import javafx.util.*;
import java.io.*;
import java.util.*;
import java.lang.*;
interface Deals_and_Carts
{
    void showCoupons();
    void showCart();
    void emptyCart();
    void checkoutCart();
}
class Customer implements Deals_and_Carts
{
    private String name;
    private int age;
    private String phone_no;
    private String email;
    private String password;
    private double balance;
    private String status;
    LinkedList<Double> coupons=new LinkedList();
    static LinkedList<Customer> ctr=new LinkedList<Customer>();
    HashMap<Product,Integer> cart_pro=new HashMap<>();
    HashMap<Pair<LinkedList<Product>,Double[]>,Integer> cart_deal=new HashMap<>();
    Customer(){}
    Customer(String name, int age, String phone_no, String email, String password)
    {
        this.name=name;
        this.age=age;
        this.phone_no=phone_no;
        this.email=email;
        this.password=password;
        this.balance=1000;
        this.status="NORMAL";
        coupons.add(0.0);
        coupons.add(0.0);
        coupons.add(0.0);
        coupons.add(0.0);
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return this.name;
    }
    public void setBalance(double balance)
    {
        this.balance=balance;   
    }
    public double getBalance()
    {
        return this.balance;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }
    public String getStatus()
    {
        return this.status;
    }
    public void addCustomer()
    {
        Customer c=new Customer(this.name,this.age,this.phone_no,this.email,this.password);
        ctr.add(c);
    }
    public boolean isCustomer(String name, String email, String password)
    {
        for(int i=0;i<ctr.size();i++)
            if(ctr.get(i).name.equals(name) && ctr.get(i).email.equals(email) && ctr.get(i).password.equals(password))
                return true;
        return false;
    }
    public Customer getCustomer(String name, String email, String password)
    {
        for(int i=0;i<ctr.size();i++)
            if(ctr.get(i).name.equals(name) && ctr.get(i).email.equals(email) && ctr.get(i).password.equals(password))
                return ctr.get(i);
        return ctr.get(0);
    }
    public void AddProduct(Product p, int quantity)
    {
        this.cart_pro.put(p,quantity);
    }
    public void AddDeal(LinkedList<Product> ll, int quantity, Double[] red)
    {
        Double[] ob=new Double[3];
        ob[0]=(Double)(red[0]);
        ob[1]=(Double)(red[1]);
        ob[2]=(Double)(red[2]);
        Pair<LinkedList<Product>,Double[]> temp=new Pair<LinkedList<Product>,Double[]>(ll,ob);
        this.cart_deal.put(temp,quantity);
    }
    public void showCoupons()
    {
        System.out.println("Following Coupons have been Collected by Past Purchases: ");
        for(int i=0;i<this.coupons.size();i++)
            if(this.coupons.get(i)>0)
                System.out.println(this.coupons.get(i)+"%");
    }
    public void showCart()
    {
        System.out.println();
        if(cart_pro.size()==0 && cart_deal.size()==0)
        {
            System.out.println("Cart is Empty.");
            return;
        }
        for(Product pro:cart_pro.keySet())
            System.out.println(pro.getName()+": "+cart_pro.get(pro));
        for(Pair<LinkedList<Product>,Double[]> pro:cart_deal.keySet())
        {
            LinkedList<Product> ll=pro.getKey();
            System.out.println(ll.get(0).getName()+" + "+ll.get(1).getName()+": "+cart_deal.get(pro));
        }
    }
    public void emptyCart()
    {
        cart_pro.clear();
        cart_deal.clear();
        System.out.println("\nAll Items have been Removed from the Cart.");
    }
    public boolean isAvailable()
    {
        HashMap<Product,Integer> temp=new HashMap<>();
        for(Product pro:cart_pro.keySet())
            temp.put(pro,cart_pro.get(pro));
        for(Pair<LinkedList<Product>,Double[]> pro:cart_deal.keySet())
        {
            LinkedList<Product> ll=pro.getKey();
            if(temp.containsKey(ll.get(0)))
                temp.replace(ll.get(0),temp.get(ll.get(0))+cart_deal.get(pro));
            else
                temp.put(ll.get(0),cart_deal.get(pro));
            if(temp.containsKey(ll.get(1)))
                temp.replace(ll.get(1),temp.get(ll.get(1))+cart_deal.get(pro));
            else
                temp.put(ll.get(1),cart_deal.get(pro));
        }
        for(Product p:temp.keySet())
            if(p.getQuantity()<temp.get(p))
                return false;
        return true;
    }
    public void checkoutCart()
    {
        System.out.println("\nFinal Details of the Order are: ");
        Collections.sort(coupons);
        double amt=0;
        double ini=0;
        double disc1,disc3=coupons.get(coupons.size()-1);
        int idx;
        boolean flag=false;
        if(this.status.equals("ELITE"))
        {
            idx=2;
            disc1=10;
        }
        else if(this.status.equals("PRIME"))
        {
            idx=1;
            disc1=5;
        }
        else
        {
            idx=0;
            disc1=0;
        }
        for(Product pro:cart_pro.keySet())
        {
            double max=Math.max(disc1,disc3);
            max=Math.max(max,pro.disc_per[idx]);
            if(max==disc3)
                flag=true;
            System.out.println("\nProduct Name: "+pro.getName());
            System.out.println("Product ID: "+pro.getID());
            System.out.println("Original Price: "+pro.getPrice()*cart_pro.get(pro));
            System.out.println("Discount Price: "+pro.getPrice()*cart_pro.get(pro)*(1-max/100));
            ini+=pro.getPrice()*cart_pro.get(pro);
            amt+=pro.getPrice()*cart_pro.get(pro)*(1-max/100);
        }
        for(Pair<LinkedList<Product>,Double[]> pro:cart_deal.keySet())
        {
            double max=Math.max(disc1,disc3);
            if(max==disc3)
                flag=true;
            LinkedList<Product> ll=pro.getKey();
            System.out.println("\nProduct 1 Name: "+ll.get(0).getName());
            System.out.println("Product 1 ID: "+ll.get(0).getID());
            System.out.println("Product 2 Name: "+ll.get(1).getName());
            System.out.println("Product 2 ID: "+ll.get(1).getID());
            System.out.println("Original Price: "+pro.getValue()[idx]);
            System.out.println("Discount Price: "+pro.getValue()[idx]*(1-max/100));
            ini+=pro.getValue()[idx];
            amt+=pro.getValue()[idx]*(1-max/100);
        }
        double del=0;
        if(this.status.equals("ELITE"))
            del=100;
        else if(this.status.equals("PRIME"))
            del=100+0.02*ini;
        if(this.status.equals("NORMAL"))
            del=100+0.05*ini;
        System.out.println("\nDelivery Charges: "+del);
        amt+=del;
        System.out.println("\nTotal Amount: "+amt);
        if(amt>this.balance)
        {
            System.out.println("You do not have enough Balance to Checkout Cart. Please add more money to your Wallet and try again.");
            return;
        }
        Random rand=new Random();
        if(status.equals("ELITE"))
        {
            if(flag==true)
                coupons.remove(coupons.size()-1);
            System.out.println("Order Placed. Will be Delivered in 2 Days.");
            if(amt>5000)
            {
                System.out.println("\nNew Coupons Collected");
                for(int i=0;i<4;i++)
                {
                    int random=rand.nextInt(11)+5;
                    coupons.add((double)random);
                    System.out.println((double)random+"%");
                }
            }
            int random=rand.nextInt(2);
            if(random==1)
            {
                Product p=new Product();
                p=p.surpriseGift();
                System.out.println("\nCongratulations!!! You have received a free product as a surprise.");
                System.out.println("Product Name: "+p.getName());
            }
        }
        else if(status.equals("PRIME"))
        {
            if(flag==true)
                coupons.remove(coupons.size()-1);
            System.out.println("Order Placed. Will be Delivered in 3-6 Days.");
            if(amt>5000)
            {
                System.out.println("\nNew Coupons Collected");
                for(int i=0;i<2;i++)
                {
                    int random=rand.nextInt(11)+5;
                    coupons.add((double)random);
                    System.out.println((double)random+"%");
                }
            }
        }
        else
            System.out.println("Order Placed. Will be Delivered in 7-10 Days.");
        this.balance-=amt;
        for(Product pro:cart_pro.keySet())
            pro.setQuantity(pro.getQuantity()-cart_pro.get(pro));
        for(Pair<LinkedList<Product>,Double[]> pro:cart_deal.keySet())
        {
            LinkedList<Product> ll=pro.getKey();
            ll.get(0).setQuantity(ll.get(0).getQuantity()-cart_deal.get(pro));
            ll.get(1).setQuantity(ll.get(1).getQuantity()-cart_deal.get(pro));
        }
        emptyCart();
    }   
    public void deleteItems()
    {
        Scanner input=new Scanner(System.in);
        this.showCart();
        System.out.print("\nEnter the Number of Individual Items to Delete: ");
        int num=input.nextInt();
        for(int i=0;i<num;i++)
        {
            System.out.print("Enter the Product ID: ");
            String id=input.next();
            for(Product pro:cart_pro.keySet())
            {
                if(pro.getID().equals(id))
                {
                    cart_pro.remove(pro);
                    break;
                }
            }
        }
        System.out.print("\nEnter the Number of Deals to Delete: ");
        num=input.nextInt();
        for(int i=0;i<num;i++)
        {
            System.out.print("Enter Product ID 1: ");
            String id_1=input.next();
            System.out.print("Enter Product ID 2: ");
            String id_2=input.next();
            for(Pair<LinkedList<Product>,Double[]> pro:cart_deal.keySet())
            {
                LinkedList<Product> ll=pro.getKey();
                if(ll.get(0).getID().equals(id_1) && ll.get(1).getID().equals(id_2) || ll.get(0).getID().equals(id_2) && ll.get(1).getID().equals(id_1))
                {
                    cart_deal.remove(pro);
                    break;
                }
            }
        }
    }
}

class Admin
{
    String username;
    String password;
    public boolean CheckCredentials(String uname,String pword)
    {
        if(uname.equals("Ashlesha Gupta") && pword.equals("2021380"))
            return true;
        return false;
    }
}

class Category extends Admin
{
    private String cat_id;
    private String cat_name;
    Category(){}
    Category(String cat_id,String cat_name)
    {
        this.cat_id=cat_id;
        this.cat_name=cat_name;
    }
    public void setID(String id)
    {
        this.cat_id=id;
    }
    public String getID()
    {
        return this.cat_id;
    }
    public void setName(String name)
    {
        this.cat_name=name;
    }
    public String getName()
    {
        return this.cat_name;
    }
}
class Product extends Category
{
    private String pro_name;
    private String pro_id;
    private double price;
    private int quantity;
    double[] disc_per=new double[3];
    static HashMap<Category,LinkedList<Product> > catalog=new HashMap<>();
    static HashMap<LinkedList<Product>,Double[]> deal=new HashMap<>();
    Product(){}
    Product(String pro_name,String pro_id,double price,int quantity)
    {
        this.pro_name=pro_name;
        this.pro_id=pro_id;
        this.price=price;
        this.quantity=quantity;
        this.disc_per[0]=0;//Normal
        this.disc_per[1]=0;//Prime
        this.disc_per[2]=0;//Elite
    }
    public void setName(String name)
    {
        this.pro_name=name;
    }
    public String getName()
    {
        return this.pro_name;
    }
    public void setID(String id)
    {
        this.pro_id=id;
    }
    public String getID()
    {
        return this.pro_id;
    }
    public void setPrice(double price)
    {
        this.price=price;
    }
    public double getPrice()
    {
        return this.price;
    }
    public void setQuantity(int q)
    {
        this.quantity=q;
    }
    public int getQuantity()
    {
        return this.quantity;
    }
    public boolean isCatPresent(String id)
    {
        if(catalog.size()==0)
            return true;
        for(Category c:catalog.keySet())
            if(c.getID().equals(id))
                return false;
        return true;
    }
    public void AddCategory(Category c)
    {
        LinkedList<Product> temp=new LinkedList<>();
        temp.add(this);
        catalog.put(c,temp);
    }
    public void AddProduct(String id)
    {
        for(Category c:catalog.keySet())
            if(c.getID().equals(id))
            {
                LinkedList temp=catalog.get(c);
                temp.add(this);
                return;
            }
    }
    public void DeleteCategory(String id, String name)
    {
        for(Category c:catalog.keySet())
            if(c.getID().equals(id) && c.getName().equals(name))
            {
                System.out.println("\n"+c.getName()+" Category has been Deleted.");
                catalog.remove(c);
                return;
            }
    }
    public void DeleteProduct(String id, String name)
    {
        for(Category c:catalog.keySet())
        {
            if(c.getName().equals(name))
            {
                LinkedList<Product> temp=catalog.get(c);
                for(int i=0;i<temp.size();i++)
                {
                    if(temp.get(i).pro_id.equals(id))
                    {
                        if(temp.size()==1)
                            System.out.println("\nThere is only one Product in this Category.\nIf you want to Delete this Product, choose to Delete Category.\nOtherwise, add another Product and then Delete this one.");
                        else
                        {
                            temp.remove(i);
                            System.out.println("\nProduct has been Deleted.");
                        }
                        return;
                    }
                }
            }
        }
    }
    public void AddDiscount(String id,double dn,double dp,double de)
    {
        for(Category c:catalog.keySet())
        {
            LinkedList<Product> temp=catalog.get(c);
            for(int i=0;i<temp.size();i++)
            {
                if(temp.get(i).pro_id.equals(id))
                {
                    temp.get(i).disc_per[0]=dn;
                    temp.get(i).disc_per[1]=dp;
                    temp.get(i).disc_per[2]=de;
                    return;
                }
            }
        }
    }
    public void GiveawayDeal(String id_1,String id_2,double r0,double r1,double r2)
    {
        Double[] red=new Double[3];
        red[0]=r0;
        red[1]=r1;
        red[2]=r2;
        LinkedList<Product> temp=new LinkedList<>();
        for(Category c:catalog.keySet())
        {
            LinkedList<Product> t=catalog.get(c);
            for(int i=0;i<t.size();i++)
            {
                if(t.get(i).pro_id.equals(id_1) || t.get(i).pro_id.equals(id_2))
                    temp.add(t.get(i));
            }
        }
        deal.put(temp,red);
    }
    public void ShowProducts()
    {
        boolean flag=false;
        for(Category c:catalog.keySet())
        {
            flag=true;
            System.out.println("\nCategory Name: "+c.getName());
            System.out.println("Category ID: "+c.getID());
            LinkedList<Product> temp=catalog.get(c);
            for(int i=0;i<temp.size();i++)
            {
                System.out.println("\nProduct Name: "+temp.get(i).pro_name);
                System.out.println("Product ID: "+temp.get(i).pro_id);
                System.out.println("Price: "+temp.get(i).price);
                System.out.println("Quantity: "+temp.get(i).quantity);
            }
        }
        if(!flag)
            System.out.println("\nThere are no Products.");
    }
    public void ShowDeals()
    {
        int q=1000000000;
        boolean flag=false;
        for(LinkedList<Product> temp:deal.keySet())
        {
            flag=true;
            for(int i=0;i<temp.size();i++)
            {
                System.out.println("\nProduct "+(i+1)+" Name: "+temp.get(i).pro_name);
                System.out.println("Product "+(i+1)+" ID: "+temp.get(i).pro_id);
                q=Math.min(q,temp.get(i).quantity);
            }
            System.out.println("Quantity: "+q);
            System.out.println("\nCombined Price:");
            System.out.println("For NORMAL: "+deal.get(temp)[0]);
            System.out.println("For PRIME: "+deal.get(temp)[1]);
            System.out.println("For ELITE: "+deal.get(temp)[2]);
        }
        if(!flag)
            System.out.println("\nThere are no Deals Available.");
    }
    public Product findProduct(String id)
    {
        Product p=new Product();
        for(Category c:catalog.keySet())
        {
            LinkedList<Product> temp=catalog.get(c);
            for(int i=0;i<temp.size();i++)
            {
                if(temp.get(i).pro_id.equals(id))
                    return temp.get(i);
            }
        }
        return p;
    }
    public int FindDeal(String id_1,String id_2)
    {
        int q=1000000000;
        for(LinkedList<Product> temp:deal.keySet())
        {
            if(temp.get(0).pro_id.equals(id_1) && temp.get(1).pro_id.equals(id_2) || temp.get(0).pro_id.equals(id_2) && temp.get(1).pro_id.equals(id_1))
            {
                q=Math.min(temp.get(0).quantity,temp.get(1).quantity);
                return q;
            }
        }
        return 0;
    }
    public LinkedList<Product> FindDeal(String id_1,String id_2,int q)
    {
        LinkedList<Product> ll=new LinkedList<>();
        for(LinkedList<Product> temp:deal.keySet())
        {
            if(temp.get(0).pro_id.equals(id_1) && temp.get(1).pro_id.equals(id_2) || temp.get(0).pro_id.equals(id_2))
                return temp;
        }
        return ll;
    }
    public Double[] FindDeal(int q, String id_1, String id_2)
    {
        Double[] val=new Double[3];
        for(LinkedList<Product> temp:deal.keySet())
        {
            if(temp.get(0).pro_id.equals(id_1) && temp.get(1).pro_id.equals(id_2) || temp.get(0).pro_id.equals(id_2))
                return deal.get(temp);
        }
        return val;
    }
    public Product surpriseGift()
    {
        Product p=new Product();
        for(Category c:catalog.keySet())
        {
            LinkedList<Product> temp=catalog.get(c);
            for(int i=0;i<temp.size();i++)
                if(temp.get(i).quantity>1)
                {
                    temp.get(i).quantity--;
                    return temp.get(i);
                }
        }
        return p;
    }
}

public class assignment
{
    static void Admin_1()
    {
        Scanner input=new Scanner(System.in);
        Product p=new Product();
        System.out.print("\nAdd Category ID: ");
        String cat_id=input.nextLine();
        if(!p.isCatPresent(cat_id))
        {
            System.out.println("\nDear Admin, this Category is already Present. Choose to Add a Category Again.");
            return;
        }
        System.out.print("Add the Name of the Category: ");
        String cat_name=input.nextLine();
        System.out.println("\nAdd a Product:");
        System.out.print("Product Name: ");
        String pro_name=input.nextLine();
        System.out.print("Product Details: ");
        String pro_id=input.nextLine();
        System.out.print("Product ID: ");
        pro_id=input.nextLine();
        System.out.print("Price: ");
        double price=input.nextDouble();
        System.out.print("Quantity: ");
        int quantity=input.nextInt();
        p=new Product(pro_name,pro_id,price,quantity);
        Category c=new Category(cat_id,cat_name);
        p.AddCategory(c);
    }
    static void Admin_2()
    {
        Scanner input=new Scanner(System.in);
        Product p=new Product();
        System.out.print("\nEnter Category ID: ");
        String cat_id=input.nextLine();
        System.out.print("Enter the Name of the Category: ");
        String cat_name=input.nextLine();
        p.DeleteCategory(cat_id,cat_name);
    }
    static void Admin_3()
    {
        Scanner input=new Scanner(System.in);
        Product p=new Product();
        System.out.print("\nEnter Category ID: ");
        String cat_id=input.nextLine();
        if(p.isCatPresent(cat_id))
        {
            System.out.println("\nThis Category is not Present. Please Add Category before adding Product.");
            return;
        }
        System.out.println("\nAdd a Product:");
        System.out.print("Product Name: ");
        String pro_name=input.nextLine();
        System.out.print("Product Details: ");
        String pro_id=input.nextLine();
        System.out.print("Product ID: ");
        pro_id=input.nextLine();
        System.out.print("Price: ");
        double price=input.nextDouble();
        System.out.print("Quantity: ");
        int quantity=input.nextInt();
        p=new Product(pro_name,pro_id,price,quantity);
        p.AddProduct(cat_id);
    }
    static void Admin_4()
    {
        Scanner input=new Scanner(System.in);
        Product p=new Product();
        System.out.print("\nEnter the Name of the Category: ");
        String cat_name=input.nextLine();
        System.out.print("Enter the Product ID: ");
        String pro_id=input.nextLine();
        p.DeleteProduct(pro_id,cat_name);
    }
    static void Admin_5()
    {
        Scanner input=new Scanner(System.in);
        Product p=new Product();
        System.out.print("\nEnter Product ID to Add Discount: ");
        String id=input.nextLine();
        System.out.print("Enter Discount for NORMAL Customers: ");
        double d_1=input.nextDouble();
        System.out.print("Enter Discount for PRIME Customers: ");
        double d_2=input.nextDouble();
        System.out.print("Enter Discount for ELITE Customers: ");
        double d_3=input.nextDouble();
        p.AddDiscount(id,d_1,d_2,d_3);
    }
    static void Admin_6()
    {
        Scanner input=new Scanner(System.in);
        Product p=new Product();
        System.out.print("Enter the First Product ID for Giveaway Deal: ");
        String id_1=input.nextLine();
        System.out.print("Enter the Second Product ID for Giveaway Deal: ");
        String id_2=input.nextLine();
        System.out.println("Enter the Combined Price (Lesser than their Combined Price):");
        System.out.print("For NORMAL: ");
        double r0=input.nextDouble();
        System.out.print("For PRIME: ");
        double r1=input.nextDouble();
        System.out.print("For ELITE: ");
        double r2=input.nextDouble();
        p.GiveawayDeal(id_1,id_2,r0,r1,r2);
    }
    static void Admin()
    {
        Scanner input=new Scanner(System.in);
        Console cnsl=System.console();
        Admin a=new Admin();
        System.out.println("\nDear Admin, Please Enter your Username and Password");
        System.out.print("Enter your Username: ");
        String uname=input.nextLine();
        System.out.print("Enter your Password: ");
        char[] ch=cnsl.readPassword();
        String password="";
        for(int i=0;i<ch.length;i++)
            password+=ch[i];
        if(a.CheckCredentials(uname,password))
            System.out.println("\nWelcome "+uname+"!!!");
        else
        {
            System.out.println("\nWrong Username or Password");
            return;
        }
        while(true)
        {
            System.out.println("\n1. Add Category");
            System.out.println("2. Delete Category");
            System.out.println("3. Add Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Set Discount on Product");
            System.out.println("6. Add Giveaway Deal");
            System.out.println("7. Back");
            System.out.print("Press 1-7: ");
            int mode=input.nextInt();
            boolean flag=false;
            switch(mode)
            {
                case 1:
                    Admin_1();
                    break;
                case 2:
                    Admin_2();
                    break;
                case 3:
                    Admin_3();
                    break;
                case 4:
                    Admin_4();
                    break;
                case 5:
                    Admin_5();
                    break;
                case 6:
                    Admin_6();
                    break;
                case 7:
                    flag=true;
                    break;
            }
            if(flag)
                break;
        }
    }
    
    static void Customer_1()
    {
        Scanner input=new Scanner(System.in);
        Console cnsl=System.console();
        System.out.print("\nEnter Your Name: ");
        String name=input.nextLine();
        System.out.print("Enter Your Age: ");
        int age=input.nextInt();
        System.out.print("Enter Your Phone Number: ");
        String phone_no=input.next();
        System.out.print("Enter Your E-Mail ID: ");
        String email=input.next();
        System.out.print("Enter Your Password: ");
        char[] ch=cnsl.readPassword();
        String password="";
        for(int i=0;i<ch.length;i++)
            password+=ch[i];
        Customer c=new Customer(name,age,phone_no,email,password);
        c.addCustomer();
        System.out.println("\nCustomer Successfully Registered!!!");
    }
    static void Customer_2_1(Customer c)
    {
        Product p=new Product();
        System.out.println("\nNote that Products are shown at MRP here.\nOffers and Discounts will be Applied According to Availability of Coupons and Customer Status.");
        p.ShowProducts();
    }
    static void Customer_2_2(Customer c)
    {
        Product p=new Product();
        System.out.println("\nNote that Products are shown at MRP here.\nOffers and Discounts will be Applied According to Availability of Coupons and Customer Status.");
        p.ShowDeals();
    }
    static void Customer_2_3(Customer c)
    {
        Scanner input=new Scanner(System.in);
        Product p=new Product();
        System.out.print("\nEnter the Product ID: ");   
        String id=input.nextLine();
        System.out.print("Enter quantity: ");
        int quantity=input.nextInt();
        p=p.findProduct(id);
        if(p.getQuantity()<quantity)
            System.out.println("\nSelected Quantity of Product is not Available.");
        else
            c.AddProduct(p,quantity);
    }
    static void Customer_2_4(Customer c)
    {
        Scanner input=new Scanner(System.in);
        System.out.print("\nEnter Product ID 1: ");   
        String id_1=input.nextLine();
        System.out.print("Enter Product ID 2: ");   
        String id_2=input.nextLine();
        System.out.print("Enter quantity: ");
        int quantity=input.nextInt();
        Product p=new Product();
        int q=p.FindDeal(id_1,id_2);
        if(quantity>q)
            System.out.println("\nSelected Quantity of Product is not Available.");
        else
        {
            LinkedList<Product>ll=p.FindDeal(id_1,id_2,q);
            Double[] red=p.FindDeal(q,id_1,id_2);
            c.AddDeal(ll,quantity,red);
        }
    }
    static void Customer_2_5(Customer c)
    {
        System.out.println("\nMaximum Percentage of Discount will be Applied.");
        if(c.getStatus().equals("ELITE"))
        {
            System.out.println("\nAll ELITE Customers will Enjoy a discount on each Product on Checkout as set by the Admin.");   
            System.out.println("A 10% Discount is given to all ELITE Customers");
            c.showCoupons();
        }
        else if(c.getStatus().equals("PRIME"))
        {
            System.out.println("\nAll PRIME Customers will Enjoy a discount on each Product on Checkout as set by the Admin.");    
            System.out.println("A 5% Discount is given to all PRIME Customers");
            c.showCoupons();
        }
        else
        {
            System.out.println("\nAll NORMAL Customers will Enjoy a discount on each Product on Checkout as set by the Admin.");
            c.showCoupons();
        }
    }
    static void Customer_2_6(Customer c)
    {
        System.out.println("\nCurrent Account Balance is Rs."+c.getBalance());
    }
    static void Customer_2_7(Customer c)
    {
        c.showCart();
    }
    static void Customer_2_8(Customer c)
    {
        c.emptyCart();
    }
    static void Customer_2_9(Customer c)
    {
        Scanner input=new Scanner(System.in);
        if(c.isAvailable())
        {
            System.out.println("\n1. Proceed to pay");
            System.out.println("2. Delete Items");
            System.out.println("3. Back");
            System.out.print("Press 1-3: ");
            int mode=input.nextInt();
            if(mode==1)
                c.checkoutCart();
            else if(mode==2)
                c.deleteItems();
            else
                return;
        }
        else
            System.out.println("\nSome Products are Out of Stock. Please Empty Cart and Add Products again According to Availability.");
    }
    static void Customer_2_10(Customer c)
    {
        Scanner input=new Scanner(System.in);
        System.out.println("\nCurrent Status: "+c.getStatus());
        System.out.print("Choose New Status (ELITE/PRIME): ");
        String status=input.next();
        System.out.println("1. Monthly Subscription");
        System.out.println("2. Yearly Subscription");
        System.out.print("Press 1 or 2: ");
        int ch=input.nextInt();
        int amt=0;
        if(status.equals("ELITE"))
        {
            if(ch==1)
                amt=300;
            else
                amt=3600;
        }
        if(status.equals("PRIME"))
        {
            if(ch==1)
                amt=200;
            else
                amt=2400;
        }
        if(amt>c.getBalance())
        {
            System.out.println("\nNot Enough Balance to Upgrade Status to "+status);
            return;
        }
        else
        {
            System.out.println("\nStatus Upgraded to "+status);
            c.setBalance(c.getBalance()-amt);
            c.setStatus(status);
        }
    }
    static void Customer_2_11(Customer c)
    {
        Scanner input=new Scanner(System.in);
        System.out.print("\nEnter the Amount to Add to Wallet: ");
        double amt=input.nextDouble();
        c.setBalance(c.getBalance()+amt);
    }
    static void Customer_2()
    {
        Scanner input=new Scanner(System.in);
        Console cnsl=System.console();
        System.out.print("\nEnter Your Name: ");
        String name=input.nextLine();
        System.out.print("Enter Your E-Mail ID: ");
        String email=input.next();
        System.out.print("Enter Your Password: ");
        char[] ch=cnsl.readPassword();
        String password="";
        for(int i=0;i<ch.length;i++)
            password+=ch[i];
        Customer c=new Customer();
        if(c.isCustomer(name,email,password))
            c=c.getCustomer(name,email,password);
        else
        {
            System.out.println("\nIncorrect Login Details");
            return;
        }
        System.out.println("\nWelcome "+c.getName()+"!!!");
        while(true)
        {
            System.out.println("\n1. Browse Products");
            System.out.println("2. Browse Deals");
            System.out.println("3. Add a Product to Cart");
            System.out.println("4. Add Products in Deal to Cart");
            System.out.println("5. View Coupons");
            System.out.println("6. Check Account Balance");
            System.out.println("7. View Cart");
            System.out.println("8. Empty Cart");
            System.out.println("9. Checkout Cart");
            System.out.println("10. Upgrade Customer Status");
            System.out.println("11. Add Amount to Wallet");
            System.out.println("12. Back");
            System.out.print("Press 1-12: ");
            int mode=input.nextInt();
            boolean flag=false;
            switch(mode)
            {
                case 1:
                    Customer_2_1(c);
                    break;
                case 2:
                    Customer_2_2(c);
                    break;
                case 3:
                    Customer_2_3(c);
                    break;
                case 4:
                    Customer_2_4(c);
                    break;
                case 5:
                    Customer_2_5(c);
                    break;
                case 6:
                    Customer_2_6(c);
                    break;
                case 7:
                    Customer_2_7(c);
                    break;
                case 8:
                    Customer_2_8(c);
                    break;
                case 9:
                    Customer_2_9(c);
                    break;
                case 10:
                    Customer_2_10(c);
                    break;
                case 11:
                    Customer_2_11(c);
                    break;
                case 12:
                    flag=true;
                    break;
            }
            if(flag)
                break;
        }
    }
    static void Customer()
    {
        while(true)
        {
            Scanner input=new Scanner(System.in);
            System.out.println("\n1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Back");
            System.out.print("Press 1-3: ");
            int mode=input.nextInt();
            boolean flag=false;
            switch(mode)
            {
                case 1:
                    Customer_1();
                    break;
                case 2:
                    Customer_2();
                    break;
                case 3:
                    flag=true;
                    break;
            }
            if(flag)
                break;
        }
    }

    public static void main(String[] args)
    {
        Scanner input=new Scanner(System.in);
        while(true)
        {
            System.out.println("\nWELCOME TO FLIPZON");
            System.out.println("1. Enter as Admin");
            System.out.println("2. Explore the Product Catalog");
            System.out.println("3. Show Available Deals");
            System.out.println("4. Enter as Customer");
            System.out.println("5. Exit the Application");
            System.out.print("Press 1-5: ");
            int mode=input.nextInt();
            boolean flag=false;
            switch(mode)
            {
                case 1:
                    Admin();
                    break;
                case 2:
                    Product p=new Product();
                    p.ShowProducts();
                    break;
                case 3: 
                    p=new Product();
                    p.ShowDeals();
                    break;
                case 4:
                    Customer();
                    break;
                case 5:
                    flag=true;
                    break;
            }
            if(flag)
            {
                System.out.println("\nThank You for using FLIPZON!!!");
                break;
            }
        }
    }
}