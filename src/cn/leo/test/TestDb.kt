package cn.leo.test

import cn.leo.kotlin.db.MysqlHelper
import cn.leo.kotlin.utils.DbUtil

fun main(args: Array<String>) {
    testInsert()
//    testDelete()
//    testUpdate()
//    testQuery()
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
