package com.mawujun.repository.identity.generator;

public class SnowFlake {
	private static final long START_STMP = 1480166465631L;
	private static final long SEQUENCE_BIT = 12L;
	private static final long MACHINE_BIT = 5L;
	private static final long DATACENTER_BIT = 5L;
	private static final long MAX_DATACENTER_NUM = 31L;
	private static final long MAX_MACHINE_NUM = 31L;
	private static final long MAX_SEQUENCE = 4095L;
	private static final long MACHINE_LEFT = 12L;
	private static final long DATACENTER_LEFT = 17L;
	private static final long TIMESTMP_LEFT = 22L;
	private long datacenterId = 31L;
	private long machineId = 31L;
	private long sequence = 0L;
	private long lastStmp = -1L;
	private long increment = 1L;
	private long sequenceStart = 0L;

	public SnowFlake() {
	}

	public SnowFlake(long datacenterId, long machineId) {
		if (datacenterId <= 31L && datacenterId >= 0L) {
			if (machineId <= 31L && machineId >= 0L) {
				this.datacenterId = datacenterId;
				this.machineId = machineId;
			} else {
				throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
			}
		} else {
			throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
		}
	}

	public SnowFlake(long datacenterId, long machineId, long increment, long sequenceStart) {
		if (increment < 1L) {
			increment = 1L;
		}

		if (datacenterId <= 31L && datacenterId >= 0L) {
			if (machineId <= 31L && machineId >= 0L) {
				if (increment > 4095L) {
					throw new IllegalArgumentException("increment can't be greater than MAX_SEQUENCE");
				} else {
					this.datacenterId = datacenterId;
					this.machineId = machineId;
					this.increment = increment;
					this.sequenceStart = sequenceStart;
				}
			} else {
				throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
			}
		} else {
			throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
		}
	}

	public SnowFlake inti(long datacenterId, long machineId, long increment, long sequenceStart) {
		if (datacenterId <= 31L && datacenterId >= 0L) {
			if (machineId <= 31L && machineId >= 0L) {
				if (increment <= 4095L && increment >= 1L) {
					this.datacenterId = datacenterId;
					this.machineId = machineId;
					this.increment = increment;
					this.sequenceStart = sequenceStart;
					return this;
				} else {
					throw new IllegalArgumentException("increment can't be greater than MAX_SEQUENCE or less than 1");
				}
			} else {
				throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
			}
		} else {
			throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
		}
	}

	public synchronized long nextId() {
		long currStmp = this.getNewstmp();
		if (currStmp < this.lastStmp) {
			throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
		} else {
			if (currStmp == this.lastStmp) {
				this.sequence = this.sequence + this.increment & 4095L;
				if (this.sequence == 0L || this.sequence <= this.sequenceStart) {
					currStmp = this.getNextMill();
				}
			} else {
				this.sequence = this.sequenceStart;
			}

			this.lastStmp = currStmp;
			return currStmp - 1480166465631L << 22 | this.datacenterId << 17 | this.machineId << 12 | this.sequence;
		}
	}

	private long getNextMill() {
		long mill;
		for (mill = this.getNewstmp(); mill <= this.lastStmp; mill = this.getNewstmp()) {
			;
		}

		return mill;
	}

	private long getNewstmp() {
		return System.currentTimeMillis();
	}

	public long getDatacenterId() {
		return this.datacenterId;
	}

	public long getMachineId() {
		return this.machineId;
	}

	public long getIncrement() {
		return this.increment;
	}

	public long getSequence() {
		return this.sequence;
	}

	public long getLastStmp() {
		return this.lastStmp;
	}

	public long getDatacenterById(Long id) {
		return id >> 17 & 31L;
	}

	public long getMachineIdById(Long id) {
		return id >> 12 & 31L;
	}

	public static void main(String[] args) {
		SnowFlake snowFlake = new SnowFlake(31L, 31L);
		Long id = snowFlake.nextId();
		long start = System.currentTimeMillis();

		for (int i = 0; i < 2200; ++i) {
			id = snowFlake.nextId();
			System.out.println(id);
			System.out.println(Long.toBinaryString(id));
			System.out.println("datacenterid:" + snowFlake.getDatacenterId() + " machineid:" + snowFlake.getMachineId()
					+ " lastStmp:" + snowFlake.getLastStmp() + " sequence:" + snowFlake.getSequence());
			System.out.println(
					"datacenterid:" + snowFlake.getDatacenterById(id) + " machineid:" + snowFlake.getMachineIdById(id));
		}

		System.out.println(System.currentTimeMillis() - start);
	}
}
