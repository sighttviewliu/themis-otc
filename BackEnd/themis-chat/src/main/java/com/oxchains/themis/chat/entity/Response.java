package com.oxchains.themis.chat.entity;

/**
 * 回复消息
 *
 * @author -brandon-
 */
public class Response {

    /**
     * 模块号
     */
    private short module;

    /**
     * 命令号
     */
    private short cmd;

    /**
     * 结果码
     */
    private int stateCode = 1;

    /**
     * 数据
     */
    private Object data;

    public Response() {
    }

    public Response(Request message) {
        this.module = message.getModule();
        this.cmd = message.getCmd();
    }

    public Response(short module, short cmd, Object data) {
        this.module = module;
        this.cmd = cmd;
        this.data = data;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public short getModule() {
        return module;
    }

    public void setModule(short module) {
        this.module = module;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }
}
