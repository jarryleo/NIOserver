package cn.leo.business.room;

import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.nio.utils.GsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomManager {
    //当前服务器内所有房间列表
    private static List<RoomBean> mRooms = Collections.synchronizedList(new ArrayList<RoomBean>());

    /**
     * 玩家 创建 房间
     *
     * @param user 创建房间的玩家，房主
     */
    public static void createRoom(UserBean user) {
        //创建房间对象
        RoomBean room = new RoomBean();
        //设置房间id ，按服务器所有房间序号创建
        room.setRoomId(mRooms.size() + 1);
        //房主第一个进入房间
        room.addUser(user);
        //标记房主
        room.setRoomOwner(user);
        //当前画画的人
        room.setRoomPainter(user);
        //房间加入列表
        mRooms.add(room);
    }

    /**
     * 房间没人，移除房间
     *
     * @param room
     */
    public static void removeRoom(RoomBean room) {
        mRooms.remove(room);
    }

    public static String getRoomListJson() {
        return GsonUtil.toJson(mRooms);
    }

}
