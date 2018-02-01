package eccang.zookeeper_server;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.I0Itec.zkclient.ExceptionUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * 
 * @author mike_yi
 *
 * @param <T>
 */
public class DistributeSimpleQueue<T> {
	protected ZkClient zkClient;
	protected final String root;// 代表根节点
	protected final static String NAME_NODE = "no_"; // 顺序节点的名字

	public DistributeSimpleQueue(ZkClient zkClient, String root) {
		this.zkClient = zkClient;
		this.root = root;
	}

	// 获取队列大小
	public int getSize() {
		return zkClient.getChildren(root).size();
	}

	// 判断队列是否为空
	public boolean isEmpty() {
		return zkClient.getChildren(root).size() == 0;
	}

	// 消息入队列
	public boolean offer(T element) {
		// 构建数据点完整位置
		String nodeFullPath = root.concat("/").concat(NAME_NODE);
		try {
			// 创建持久的节点，写入数据
			zkClient.createPersistentSequential(nodeFullPath,element);
		} catch (ZkNoNodeException e) {
			zkClient.createPersistent(root);
			offer(element);
		} catch (Exception e) {
			throw ExceptionUtil.convertToRuntimeException(e);
		}
		return true;
	}

	// 消息出队列
	@SuppressWarnings("unchecked")
	public T poll() {
		try {

			List<String> list = zkClient.getChildren(root);
			if (list.size() == 0) {
				return null;
			}
			// 将队列安装由小到大的顺序排序
			Collections.sort(list, new Comparator<String>() {
				public int compare(String lhs, String rhs) {
					return getNodeNumber(lhs, NAME_NODE).compareTo(getNodeNumber(rhs, NAME_NODE));
				}
			});

			/**
			 * 将队列中的元素做循环，然后构建完整的路径，在通过这个路径去读取数据
			 */
			for (String nodeName : list) {

				String nodeFullPath = root.concat("/").concat(nodeName);
				try {
					T node = (T) zkClient.readData(nodeFullPath);
					zkClient.delete(nodeFullPath);
					return node;
				} catch (ZkNoNodeException e) {
					// ignore
				}
			}

			return null;

		} catch (Exception e) {
			throw ExceptionUtil.convertToRuntimeException(e);
		}

	}

	private String getNodeNumber(String str, String nodeName) {
		int index = str.lastIndexOf(nodeName);
		if (index >= 0) {
			index += NAME_NODE.length();
			return index <= str.length() ? str.substring(index) : "";
		}
		return str;
	}
}
