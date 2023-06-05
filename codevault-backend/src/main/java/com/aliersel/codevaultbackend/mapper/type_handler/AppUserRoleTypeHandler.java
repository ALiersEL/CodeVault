package com.aliersel.codevaultbackend.mapper.type_handler;

import com.aliersel.codevaultbackend.constant.enums.AppUserRole;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes({JdbcType.SMALLINT})
@MappedTypes(AppUserRole.class)
public class AppUserRoleTypeHandler extends BaseTypeHandler<AppUserRole> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, AppUserRole appUserRole, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, appUserRole.getLevel());
    }

    @Override
    public AppUserRole getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int level = resultSet.getInt(s);
        return level == 0 && resultSet.wasNull()?null:AppUserRole.getRoleByLevel(level);
    }

    @Override
    public AppUserRole getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int level = resultSet.getInt(i);
        return level == 0 && resultSet.wasNull()?null:AppUserRole.getRoleByLevel(level);
    }

    @Override
    public AppUserRole getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int level = callableStatement.getInt(i);
        return level == 0 && callableStatement.wasNull()?null:AppUserRole.getRoleByLevel(level);
    }
}