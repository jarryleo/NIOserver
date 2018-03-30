package cn.leo.test

import cn.leo.kotlin.db.MysqlHelper
import cn.leo.kotlin.utils.DbUtil
import java.nio.charset.Charset

fun main(args: Array<String>) {
    var paint:String = "我是Paint"
    val bytes = paint.toByteArray(Charset.forName("utf-8"))
    val s1 = String(bytes, 0, 3, Charset.forName("utf-8"))
    println(s1)
//    testInsert()
//    testDelete()
//    testUpdate()
//    testQuery()
    //MysqlHelper().Update("person").set("name = ?", "李四").where("age = ?", 24).execute()
}

fun testInsert() {
    MysqlHelper()
            .Insert()
            .into("person")
            .values("null,?,?,?,?,?", "user1", "123456", "李思思", 24, "女")
            .execute()
}

fun testDelete() {
    MysqlHelper()
            .Delete()
            .from("person")
            .where("name = ?", "李思思")
            .execute()
}

private fun testUpdate() {
    MysqlHelper()
            .Update("person")
            .set("age = ?", 23)
            .where("name = ?", "李思思")
            .execute()
}

fun testQuery() {
    val helper = MysqlHelper()
    val resultSet = helper.Query()
            .select("*")
            .from("person")
            .where("name = ? and age = ?", "李思思", 23)
//            .limit(20)
//            .offset(10)
            .execute()
    DbUtil.show(resultSet)
    resultSet.close()
    helper.close()
}
