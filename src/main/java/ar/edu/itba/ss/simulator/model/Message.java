package ar.edu.itba.ss.simulator.model;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

public class Message {

	private UUID id;
	private int size;
	private List<Data> datagrams;

	public Message(int size, int fragmentSize) {
		super();
		this.id = UUID.randomUUID();
		this.size = size;
		this.datagrams = Lists.newLinkedList();

		int remainder = size % fragmentSize;
		int fragments = Math.floorDiv(size, fragmentSize);
		for (int i = 0; i < fragments; i++) {
			this.datagrams.add(new Data(this, fragmentSize));
		}
		if (remainder > 0)
			this.datagrams.add(new Data(this, remainder));
	}

	public UUID getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	public List<Data> getDatagrams() {
		return datagrams;
	}

	public boolean isTransferred() {
		return datagrams.stream().allMatch(data -> data.isTransferred());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + size;
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
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", size=" + size + "]";
	}

}
