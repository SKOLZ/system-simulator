package ar.edu.itba.ss.simulator.model;

import java.util.UUID;

public class Data {

	private UUID id;
	private int size;
	private Message message;
	private boolean transferred = false;

	public Data(Message message, int size) {
		super();
		this.id = UUID.randomUUID();
		this.message = message;
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public UUID getId() {
		return id;
	}

	public Message getMessage() {
		return message;
	}

	public boolean isTransferred() {
		return transferred;
	}

	public void transferred() {
		this.transferred = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + size;
		result = prime * result + (transferred ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Data other = (Data) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (size != other.size)
			return false;
		if (transferred != other.transferred)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", size=" + size + ", message=" + message
				+ ", transferred=" + transferred + "]";
	}

}
