package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("==== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("\n==== TEST 2: sellet findByDepartment ====");
		Department department = new Department(1, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller sel : list) { //Para cada seller sel na lista
			System.out.println(sel); //Imprimir o sel
		}
		
		System.out.println("\n==== TEST 3: sellet findAll ====");
		list = sellerDao.findAll();
		for (Seller sel : list) { //Para cada seller sel na lista
			System.out.println(sel); //Imprimir o sel	
		}
	}

}
