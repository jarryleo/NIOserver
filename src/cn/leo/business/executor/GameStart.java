package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.business.room.RoomManager;
import cn.leo.business.user.UserManager;

import java.nio.channels.SelectionKey;

public class GameStart implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private GameStart() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new GameStart();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {
        //开始游戏
        UserBean user = UserManager.getUser(key);
        RoomBean room = user.getRoom();
        if (room.getRoomOwner() == user) {
            //房主才能开始游戏
            startSuccess(user);
        } else {
            startFailed(key);
        }
    }

    private void startSuccess(UserBean user) {
        //返回成功消息(房间json),还要通知房间内其他人更新列表
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.GAME_START_SUC.getCode());
        MsgManager.sendMsgToRoom(user, msg, false);
    }

    private void startFailed(SelectionKey key) {
        //返回失败
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.GAME_START_FAIL.getCode());
        msg.setMsg("只有房主才能开始游戏");
        MsgManager.sendMsg(key, msg);
    }
}