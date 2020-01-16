package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
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
		
		System.out.println("\n==== TEST 4: sellet insert ====");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New ID = " + newSeller.getId());
		
		System.out.println("\n==== TEST 5: sellet update ====");
		seller = sellerDao.findById(1);
		seller.setName("Martha Waine");
		sellerDao.update(seller);
		System.out.println("Update completed");
		
		System.out.println("\n==== TEST 6: sellet delete ====");
		System.out.print("Enter ID for delete test: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completed");
		
		sc.close();
	}

}
