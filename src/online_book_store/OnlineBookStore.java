package online_book_store;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Book{
	int id;
	String title;
	String author;
	double price;
	int stock;
	List<Review> reviews;
	
	public Book(int id,String title,String author,double price,int stock) {
		this.id=id;
		this.title=title;
		this.author=author;
		this.price=price;
		this.stock=stock;
		this.reviews=new ArrayList<Review>();	
		}
	
	void addReview(Review r) {
		reviews.add(r);
	}
	
	@Override
	public String toString() {
		return "Book title:"+title+";\tBook author:"+author+
				";\tPrice:"+price+";Stock Qty:"+stock;
	}
	
	
}

class Review{
	User user;
	int rating;
	String comment;
	
	public Review(User user,int rating,String comment) {
		// TODO Auto-generated constructor stub
		this.user=user;
		this.rating=rating;
		this.comment=comment;
		
	}
}

class User{
	int id;
	String name;
	String email;
	String password;
	ShoppingCart cart;
	List<Order> orders;
	
	public User(int id,String name,String email,String password) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.name=name;
		this.email=email;
		this.password=password;
		this.cart= new ShoppingCart();
		this.orders= new ArrayList<Order>();
	}
	
}

class ShoppingCart{
	Map<Book, Integer> items=new HashMap<>();
	
	void addBook(Book b,int quantity) {
		items.put(b, items.getOrDefault(b, 0)+quantity);
	}
	
	void removeBook(Book b) {
		items.remove(b);
	}
	
	double getTotal() {
		double total=0;
		for (Map.Entry<Book, Integer> entry : items.entrySet()) {
			total+= entry.getKey().price * entry.getValue();			
		}
		
		return total;
	}
	
	void checkCart() {
		int id=1;
		for(Map.Entry<Book, Integer> entry: items.entrySet()) {
			System.out.println("BOOK:"+id+"\n"+entry.getKey().toString()+";\tCart Quantity=" + entry.getValue());
			id++;
		}
	}
}

class Order{
	static int idCounter = 1;
	int orderId;
//	List<Book> books;
	Map<Book, Integer> items;
	double amount;
	Date date;
	String status;
	
	public Order(Map<Book, Integer> items,double amount) {
		// TODO Auto-generated constructor stub
		this.orderId=idCounter++;
//		this.books=new ArrayList<>(books);
		this.items = new HashMap<>(items);
		this.amount=amount;
		this.date=new Date();
		this.status= "PLACED";
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Order ID: ").append(orderId).append("\n");

	    for (Map.Entry<Book, Integer> entry : items.entrySet()) {
	        sb.append(entry.getKey().title)
	          .append(" x ")
	          .append(entry.getValue())
	          .append("\n");
	    }

	    sb.append("Total: ").append(amount)
	      .append(", Status: ").append(status);

	    return sb.toString();
	}
	

	public String checkOutOrder() {
	    return "Order ID: " + orderId +
	           ", Amount: " + amount +
	           ", Date: " + date +
	           ", Status: " + status +
	           ", Books Count: " + items.size();
	}
}

class BookStore{
	Map<String, User> users= new HashMap<>();
	List<Book> inventory = new ArrayList<>();
	
	void registerUser(User u) {
		users.put(u.email,u);
	}
	
	User loginUser(String email,String password) {
		User user=users.get(email);
		if(user!= null && user.password.equals(password)) return user;
		
		return null;
	}
	
	List<Book> searchByTitle(String title){
		List<Book> result= new ArrayList<>();
		for(Book b:inventory) {
			if(b.title.toLowerCase().contains(title.toLowerCase())) {
				result.add(b);
			}
		}
		
		return result;
	}
	
	void addBookToInventory(Book b) {
		inventory.add(b);
	}
	
	void checkout(User u) {
//		List<Book> purchasedBooks = new ArrayList<Book>();
//		
//		for(Map.Entry<Book, Integer> entry : u.cart.items.entrySet()) {
//			Book book =entry.getKey();
//			int q=entry.getValue();
//			if(book.stock>=q) {
//				book.stock-=q;
//				purchasedBooks.add(book);
//			}
//		}
//		
//		double total=u.cart.getTotal();
//		Order order=new Order(u.cart.items, total);
//		u.orders.add(order);
//		u.cart = new ShoppingCart();
		
		double total = 0;
		Map<Book, Integer> purchasedItems = new HashMap<>();

		for (Map.Entry<Book, Integer> entry : u.cart.items.entrySet()) {
		    Book book = entry.getKey();
		    int q = entry.getValue();

		    if (book.stock >= q) {
		        book.stock -= q;
		        purchasedItems.put(book, q);
		        total += book.price * q;
		    } else {
		        System.out.println("Not enough stock for: " + book.title);
		    }
		}

		if (!purchasedItems.isEmpty()) {
		    Order order = new Order(purchasedItems, total);
		    u.orders.add(order);
		}

		u.cart = new ShoppingCart();
	}
}

public class OnlineBookStore {
	public static void main(String[] args) {
		BookStore systemBookStore= new BookStore()
;
		
		Book b1=new Book(1, "JAVA Fundamentals", "TC", 499.0, 10);
		systemBookStore.addBookToInventory(b1);
		
		
		User u1= new User(101, "HelloWord", "helloworld@gmail.com", "password");
		systemBookStore.registerUser(u1);
		
		User loggedInUser = systemBookStore.loginUser("helloworld@gmail.com", "password");
		if(loggedInUser!=null) {
			loggedInUser.cart.addBook(b1, 3);
			
			System.out.println("====Checking Cart Items=====");
			loggedInUser.cart.checkCart();
			System.out.println("====Checking Cart Items=====");
			
			System.out.println("Cart TOTAL = "+loggedInUser.cart.getTotal());
			
			System.out.println("===Stock Check before CHECKOUT===");
			System.out.println(systemBookStore.inventory.toString());
			
			systemBookStore.checkout(loggedInUser);
			System.out.println("Order placed!!");
			System.out.println("===Order Summary===");
			loggedInUser.orders.forEach(o -> System.out.println(o.checkOutOrder()));
			
			System.out.println("===Stock Check after CHECKOUT===");
			System.out.println(systemBookStore.inventory.toString());
			
			System.out.println("===User Order Details===");
//			for (Order order : loggedInUser.orders) {
//			    System.out.println(order);
//			}
//			System.out.println(loggedInUser.orders);
			loggedInUser.orders.forEach(o->System.out.println(o));
		}
		else {
			System.out.println("LogIn failed -- Invalid Credentials");
		}
		
		System.out.println("\n\n xxxx Program Ended xxxx");
	}
}
