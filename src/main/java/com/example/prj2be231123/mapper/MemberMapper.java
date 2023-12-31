package com.example.prj2be231123.mapper;

import com.example.prj2be231123.domain.Auth;
import com.example.prj2be231123.domain.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    @Insert("""
            INSERT INTO member (id, password, gender, phone, email, birthDate)
            VALUES (#{id}, #{password}, #{gender}, #{phone}, #{email}, #{birthDate})
            """)
    int insert(Member member);

    @Select("""
            SELECT id FROM member
            WHERE id = #{id}
            """)
    String selectId(String id);

    @Select("""
            SELECT *
            FROM member
            WHERE id = #{id}
            """)
    Member selectById(String id);

    @Select("""
            SELECT id, nickName, email, inserted
            FROM member
            ORDER BY inserted DESC
            """)
    List<Member> selectAll();

    @Delete("""
            DELETE FROM member
            WHERE id = #{id}
            """)
    int deleteById(String id);

    @Select("""
            SELECT nickName FROM member
            WHERE nickName = #{nickName}
            """)
    String selectByNick(String nickName);

    @Update("""
            <script>
            UPDATE member
            SET 
            <if test="password != ''">
            password = #{password},
            </if>
            nickName = #{nickName},
            phone = #{phone},
            email = #{email}
            WHERE id = #{id}
            </script>
            """)
    int update(Member member);

    @Select("""
            SELECT * FROM auth
            WHERE memberId = #{id}
            """)
    List<Auth> selectAuthId(String id);
}
