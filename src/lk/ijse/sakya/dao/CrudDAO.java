package lk.ijse.sakya.dao;

import lk.ijse.sakya.entity.SuperEntity;

import java.sql.SQLException;

public interface CrudDAO<T extends SuperEntity,ID> extends SuperDAO{
    boolean add(T obj) throws SQLException, ClassNotFoundException;
    boolean update(T obj) throws SQLException, ClassNotFoundException;
    T search(ID id) throws SQLException, ClassNotFoundException;
    boolean delete(ID id) throws SQLException, ClassNotFoundException;
}
