//package fordfulkersonscaling;

public class EdgeData {
	private double capacity;
	private double flow;
	private boolean isBackEdge;

	/**
	 * Default EdgeData with maximum capacity of 1 and current flow of 0.
	 */

	public EdgeData() {
		this.capacity = 1;
		this.flow = 0;
		this.isBackEdge = false;
		
	}

	/**
	 * Initialize EdgeData with custom values.
	 * 
	 * @param capacity
	 *            The maximum flow an edge can carry.
	 * @param flow
	 *            The current flow an edge is carrying.
	 * @throws Exception
	 *             If provided values are out of bounds.
	 */
	public EdgeData(double capacity, double flow) throws Exception {
		if (capacity < flow || flow < 0 || capacity < 0)
			throw new IndexOutOfBoundsException();
		this.capacity = capacity;
		this.flow = flow;
	}

	/**
	 * Returns the maximum capacity minus the current flow.
	 * 
	 * @return double Available capacity
	 */
	public double getAvailable() {
		return this.capacity - flow;
	}

	/**
	 * Returns the maximum capacity of this edge.
	 * 
	 * @return double Maximum capacity
	 */
	public double getEdgeCapacity() {
		return this.capacity;
	}

	/**
	 * Sets the current flow of this edge.
	 * 
	 * @param flow
	 *            New flow value this edge will carry.
	 * @throws Exception
	 *             If provided flow is out of bounds.
	 */
	public void setEdgeFlow(double flow) throws Exception {
		if (this.capacity < flow || flow < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.flow = flow;
	}

	public double getEdgeFlow() {
		// TODO Auto-generated method stub
		return this.flow;
	}

	public boolean isBackedge() {
		// TODO Auto-generated method stub
		return isBackEdge;
	}

	public void setEdgeCapacity(double capacity) {
		// TODO Auto-generated method stub
		this.capacity = capacity;

	}

	public void setBackedge(boolean b) {
		// TODO Auto-generated method stub
		this.isBackEdge = b;
		
	}
}
