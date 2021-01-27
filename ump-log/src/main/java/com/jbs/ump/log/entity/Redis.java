package com.jbs.ump.log.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Redis {
	private int maxIdle;
	private int maxTotal;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	private int timeout;
	private String ip;
	private String password;
	private int port;

}
