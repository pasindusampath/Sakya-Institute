package lk.ijse.sakya.dao;

import java.sql.SQLException;

public interface CrudDAO<T,ID> extends SuperDAO{
    boolean add(T obj) throws SQLException, ClassNotFoundException;
    boolean update(T obj) throws SQLException, ClassNotFoundException;
    T search(ID id) throws SQLException, ClassNotFoundException;
    boolean delete(ID id) throws SQLException, ClassNotFoundException;
}
