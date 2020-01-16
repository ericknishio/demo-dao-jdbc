package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
		
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, "
					+ "Email = ?, "
					+ "BirthDate = ?, "
					+ "BaseSalary = ?, "
					+ "DepartmentId = ? "
					+ "WHERE Id = ?"
					);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"DELETE FROM seller "
					+ "WHERE Id = ?"
					);
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			if(rows == 0) {
				throw new DbException("ID not found");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null; //Obj para consulta SQL, permite inserir parâmetros depois
		ResultSet rs = null; //Contém os dados armazenados em forma de tabela
		
		try {
			st = conn.prepareStatement(
					"SELECT" + 
					"	seller.*, " + 
					"	department.Name as DepName " + 
					"FROM seller " + 
					"INNER JOIN department " + 
					"ON DepartmentId = department.Id " + 
					"WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) { //Se retornar um registro
				Department dep = instantiateDepartment(rs);
				Seller sel = instantiateSeller(rs, dep);
				return sel;
			}
			return null; //Senão, nulo 
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setDepartment(dep);
		return sel;
	}
	
	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT "
					+ "seller.*, "
					+ "department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name"
					);
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);	
				}
				
				Seller sel = instantiateSeller(rs, dep);
				list.add(sel);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT " + 
					"seller.*, " + 
					"department.Name as DepName " + 
					"FROM seller " + 
					"INNER JOIN department " + 
					"ON DepartmentId = department.Id " + 
					"WHERE department.Id = ? " +
					"ORDER BY Name");
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); //Chave Id, Departament
			
			while (rs.next()) { //Percorrer o resultset enquanto tiver um próximo
				Department dep = map.get(rs.getInt("DepartmentId")); //Procura dentro de map se existe o departamento com Id informado
				
				if (dep == null) { //Se não existir
					dep = instantiateDepartment(rs); //Instanciar o departamento
					map.put(rs.getInt("DepartmentId"), dep); //Salva o departamento dentro do map
				}
				
				Seller sel = instantiateSeller(rs, dep);
				list.add(sel);
			}
			return list; //Senão, nulo 
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
