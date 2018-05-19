import java.util.*;
class SharifSelf{
	public static void main(String[] arg){
		ReserveSys reserveSys=new ReserveSys();
		Init v = new Init();
		ProcessControl p =new ProcessControl();
		v.classInit();
		v.getInitInf();
		p.mainLoop(v);
		
	}
}
//===========================================================================
class Init{
	ArrayList<Student>  studentAlist = new ArrayList<Student>();
	Buy buy=new Buy();
	Sell  sell=new Sell();
	ArrayList<Sell> sellAlist= new ArrayList<Sell>();
	ArrayList<Buy> buyAlist= new ArrayList<Buy>();
	FoodPlan foodPlan = new FoodPlan();
	Scanner datain=new Scanner(System.in);
	String command="sdfadf",s;
	String todayDate;
	String foodName;
	String date;
	String studentNum;
	int studentNumber;
	int indexOfReserveFood;
	int indexOfStudent;
	int indexOfReserveHis;
	int indexOfSell;
	Food foods = new Food();
	FoodPlan foodPlans =new FoodPlan();
	String[] strArray;
	String[] strArray2;
   Student students=new Student();
   int[] reserveDate;	

	 
   ReserveSys[] reserveSys=new ReserveSys[9];
  void classInit(){
		reserveSys[0]= new GetFood();
   	reserveSys[1]= new GetFoodForgetCode();
   	reserveSys[2]= new FoodPlan();
   	reserveSys[3]= new Reserve();
  	   reserveSys[4]= new Cancel();
   	reserveSys[5]= new Deposit();
   	reserveSys[6]= new Sell();
   	reserveSys[7]= new Buy();
   	reserveSys[8]= new GetCode();
	}


	void getInitInf(){
		
		System.out.print("Enter Today date:  ");
		this.todayDate=datain.nextLine();
		System.out.print(" Enter numbers of students:  "); 
		this.studentNumber=datain.nextInt();
		for(int i=0;i<studentNumber;i++){
			Student s1=new Student();
			s1.studentNum=datain.nextLine();
			this.studentAlist.add(s1);
		}
	}
}// end of Init class 

//=========================================================================================

class ReserveSys{
	public boolean ifForgetCodeCmd(Init v){
		if(v.strArray[2].indexOf(';')>=0)
			return true;
	 	else
			return false;
	}	
	public boolean findReserveHis( Student s , String date){
		boolean state=false;
		for(int i=0;i<s.reserveHisAlist.size();i++){
			s.reserveHis=s.reserveHisAlist.get(i);
			if(date.equals(s.reserveHis.date)){ s.indexOfReserveHis=i;state=true; break;}
		}
		return state;
	}	
	public boolean ifReserveFood(Student s , String foodName , String date){
		if(this.findReserveHis(s ,date ))
			if(s.reserveHis.foods.foodName==foodName)
				return s.reserveHis.ifResFood;
			else
				return false;
		else
			return false;

	}

	/**
	 *find specefic student from student list and update v.students object 
	 */
	public boolean findStudent(Init v, String studentNum){
		boolean state=false;
		for(int i=0;i<v.studentAlist.size();i++){
			v.students=v.studentAlist.get(i);
			if(v.students.studentNum.equals(studentNum)){
				v.indexOfStudent=i;
				state=true;
				break;
			}  
		}
		return state;	
	}
	
	public void run(Init v){
		System.out.print("running program");

	}
	public void dateSplit(String date , int[] intDate){
		String[] strArray;
		strArray=date.split("/");
		intDate[0]=Integer.parseInt(strArray[0]);
		intDate[1]=Integer.parseInt(strArray[1]);		
		intDate[2]=Integer.parseInt(strArray[2]);
	}

   public int reserveCapacityEx(Init v,String foodName,String date){
   	int reserveCnt=0;
      for(int i=0;i<v.studentAlist.size();i++){
      	v.students=v.studentAlist.get(i);
			findReserveHis(v.students,date);
         if(foodName.equals(v.students.reserveHis.foods.foodName)){
            reserveCnt++;
         }

      }
      
      return reserveCnt;
   }
	public int foodPrice(FoodPlan f,String foodName,String date){
		int x=0;
		for(int i=0;i<f.foodAlist.size();i++){
			f.foods=f.foodAlist.get(i);
			if((f.foods.cookingDate==date) && f.foods.foodName==foodName){x= f.foods.price;break;}
			else
				x= -1;	
		}
		return x;
	}

   public int wait4BuyNum(ArrayList<Buy> buyingList,String foodName){
   	int n=0;
      Buy buy=new Buy();
      for(int i=0;i<buyingList.size();i++){
      	if(foodName.equals(buy.foodName)){
         	n++;
         }
      }
      return n;
	}


}//end of ReserveSys class
//========================================================================================

class GetFoodForgetCode extends ReserveSys{		
	public void run(Init v){
		splitForgetCode(v);
		findStudent(v,v.studentNum);
		if(v.students.forgetCodeCnt<10) {
     		 v.students.sharj-=foodPrice(v.foodPlan,v.foodName,v.date);
			 v.students.forgetCodeCnt++;
			 findReserveHis(v.students,v.date);
			 v.students.reserveHisAlist.add(v.students.indexOfReserveHis,v.students.reserveHis);
			 v.studentAlist.add(v.indexOfStudent,v.students);
				
		}
	}

   private void splitForgetCode(Init v){ 
      v.strArray2=v.strArray[2].split(";");
      v.studentNum=v.strArray2[0];
      v.foodName=v.strArray2[2];
      v.date=v.strArray2[1];
   } 


}
//=======================================================================================			  
class GetFood extends ReserveSys{
		
	public void run(Init v){	
		this.commandSplit(v);
		findStudent(v,v.studentNum);
		if(ifGetFoodPermition(v.students,v.foodName,v.todayDate)){
			v.students.reserveHis.ifGetFood=true;
			v.students.reserveHisAlist.add(v.students.indexOfReserveHis,v.students.reserveHis);
			v.studentAlist.add(v.indexOfStudent,v.students);
		}
	}

	private boolean ifGetFoodPermition(Student s,String foodName, String date){
		findReserveHis(s,date);
		if( (foodName.equals(s.reserveHis.foods.foodName)) && (s.reserveHis.ifResFood) )
			return true;
		else
			return false;

	}
	private void commandSplit(Init v){
		v.studentNum=v.strArray[2];
		v.foodName=v.strArray[3];

	}
}	
//=======================================================================================
class GetCode extends ReserveSys{
	
	void printForgetCode(Init v){
		System.out.println( v.strArray[3]+";"+v.todayDate + ";" + v.strArray[4]);
	}	
	public void run(Init v){  
		this.printForgetCode(v);	
	}
}
//========================================================================================	
class Buy extends ReserveSys{												
	String foodName;
	public void run(Init v){
		this.commandSplit(v);
		if( this.findFoodFromSellList(v, v.foodName)){
			this.updateSellerStudentProfile(v,v.sell.studentNum );
			this.removeFoodFromSellList(v);
		}
	}
	private boolean findFoodFromSellList(Init v, String foodName){
		boolean state=false;
		for(int i=0;i<v.sellAlist.size();i++){
			v.sell=v.sellAlist.get(i);
			if(foodName.equals(v.sell.foodName)){
				state=true;
				v.indexOfSell=i;	
				break;
			}
		}
		return state;
	}
	private void updateSellerStudentProfile(Init v , String studentNum){
		findStudent(v, studentNum);
		findReserveHis(v.students,v.todayDate);
		v.students.reserveHis.ifGetFood=true;
		v.students.sharj+=v.sell.foodPrice;    //////////////////////////////
		v.studentAlist.add(v.indexOfStudent,v.students);
	}
	private void commandSplit(Init v){
		v.foodName=v.strArray[4];
		v.studentNum=v.strArray[3];

	}
	private void removeFoodFromSellList(Init v){
		v.sellAlist.remove(v.indexOfSell);
	}
}//end of Buy class 

//==================================================================================
class Sell extends ReserveSys{	
	String foodName;
	String studentNum;
	int foodPrice;	
	private void commandSplit(Init v,String foodName , String studentNum){
		studentNum=v.strArray[3];
		foodName=v.strArray[4];
	}	
	public void run(Init v){
		Sell s =new Sell();		
		s.commandSplit(v,s.foodName,s.studentNum);
		findStudent(v,s.studentNum);
		if(ifReserveFood(v.students,s.foodName,v.todayDate)){
   		s.foodPrice= inDayFoodPriceCal(v,s);		
			v.sellAlist.add(s);
		}
	}
	int inDayFoodPriceCal(Init v,Sell s){
		int x=0;
		x=(int)reserveCapacityEx(v,s.foodName,v.todayDate)/wait4BuyNum(v.buyAlist , s.foodName);
		return  v.students.reserveHis.foods.price*((int)(1.2+x));
		
	}
}//end of class sell	
//=====================================================================================
class Deposit extends ReserveSys{
	int sharj;
	String studentNum;
	public void run(Init v){
	this.commandSplit(v,this.studentNum,this.sharj);
	if(findStudent(v,this.studentNum)){
		v.students.sharj+=this.sharj;
		v.studentAlist.add(v.indexOfStudent,v.students);
	}	
	else 
		System.out.println("student Number is wrong!");
		
	}
 	private	void commandSplit(Init v, String StudentNum , int sharj){
		this.studentNum=v.strArray[1]; 
		sharj=Integer.parseInt(v.strArray[2]);
	
	}

}//end of class De

//=======================================================================================

class Reserve extends ReserveSys{
	int[] reserveDate;
	String foodName;
	String studentNum;
	public void run(Init v){
		commandSplit(v,this.studentNum,this.foodName,this.reserveDate);
		if(findStudent(v,this.studentNum)){
			if(!ifReserveTimeOver(v.students,v.date)&&ifSharjEnough(v.students)){
				v.students.sharj-=foodPrice(v.foodPlan,this.foodName,v.date);
				this.updateReserveHis(v.students,v );
				v.studentAlist.add(v.indexOfStudent,v.students);	
			}
		}
		else
			System.out.println("student Number is wrong!");
	}
	private boolean ifReserveTimeOver(Student s,String date){
		findReserveHis(s,date);
		if(this.reserveDate[2]>s.reserveHis.foods.deadlineDate[2]) 
			return true;
		else
			return false;
	}
	private boolean ifSharjEnough(Student s){
		if(s.sharj > -20000) 
			return true;
		else
			return false;
	}

	private void commandSplit(Init v , String studentNum , String foodName , int[] intDate){
		studentNum=v.strArray[1];
		foodName=v.strArray[3];
		v.date=v.strArray[2];
		dateSplit(v.strArray[2],intDate);
	
	}

	private void updateReserveHis(Student s, Init v){
		s.reserveHis.ifResFood=true;
		s.reserveHis.date=v.date;
		s.reserveHis.foods.foodName=this.foodName;
		s.reserveHis.foods.cookingDate=v.date;
		s.reserveHis.foods.price=foodPrice(v.foodPlan,this.foodName,v.date);
		s.reserveHisAlist.add(s.reserveHis);

	}

} //end of class
//============================================================================================

class Cancel extends Reserve{
	public void run(Init v){
		this.commandSplit(v,v.studentNum,v.foodName,v.reserveDate);
  		if(findStudent(v,v.studentNum)){
   		if(ifReserveFood(v.students,v.foodName,v.date)){
				updateStudentProfile(v.students);
				v.studentAlist.add(v.indexOfStudent,v.students);
			}
		}
	   else
        System.out.println("student Number is wrong!");
	}
   private void commandSplit(Init v , String studentNum , String foodName , int[] intDate){
      studentNum=v.strArray[2];
      foodName=v.strArray[4];
      dateSplit(v.strArray[3],intDate);
   }

	private void updateStudentProfile(Student s){
		s.reserveHis.ifGetFood=false;
      s.sharj+=(int)s.reserveHis.foods.price/2;
      s.reserveHisAlist.add(s.indexOfReserveHis,s.reserveHis);
	}
} //end of class

//=======================================================================================
class ProcessControl{
	Scanner datain =new Scanner(System.in);
	String command;
	void commandSplit(Init v){
		v.strArray=command.split(" ");
	}	
	int commandIndex(Init v){
		v.strArray=command.split(" ");
		if(v.strArray[0]=="get"&& v.strArray[1]=="food") return 0;
		else if(v.strArray[0]=="get" && v.strArray[1]=="forget") return 1;
		else if(v.strArray[0]=="add") return 2;
		else if(v.strArray[0]=="reserve") return 3;
		else if(v.strArray[0]=="cancel") return 4;	
		else if(v.strArray[0]=="deposite") return 5;
		else if(v.strArray[0]=="sell") return 6;
		else if(v.strArray[0]=="buy") return 7;
		else if(v.strArray[1]=="forget") return 8;
		else return 0;	
	}
	void mainLoop(Init v){
			
		while(!command.equals("closed")){
			command=datain.nextLine();
			commandSplit(v);
			v.reserveSys[commandIndex(v)].run(v);
			
		}//end of while("closed")
		endProcess(v);
	}

	void endProcess(Init v){	
		System.out.println("Reserve Information is:");
		for(int i=0; i<v.studentAlist.size();i++){
			v.students=v.studentAlist.get(i);
			System.out.println(v.students.studentNum + "    " + v.students.sharj+"..." + i);
			for(int j=0;j<v.students.reserveHisAlist.size();j++){
				v.students.reserveHis=v.students.reserveHisAlist.get(j);
			System.out.print(v.students.reserveHis.date + "   " + v.students.reserveHis.foods.foodName);
				if(v.students.reserveHis.ifGetFood)
					System.out.print("  " + "1");
				else
				   System.out.print("  " + "0");
				if(v.students.reserveHis.ifGetCode)
					System.out.print("  " + "1");
				else
					System.out.print("  " + "0");
			}
		
		}
		
			
	}

}//end of class process Control
//=================================================================================================
class Student{
	String studentNum;
	int forgetCodeCnt=0;
	int sharj;
	int indexOfReserveHis;
	ReserveHis reserveHis=new ReserveHis();
	ArrayList<ReserveHis> reserveHisAlist = new ArrayList<ReserveHis>();

}

class Food{
	String foodName;
	int price;
	int[] deadlineDate=new int[3];
	String cookingDate;
}
class ReserveHis{
	String date;
	Food foods=new Food();
	boolean ifResFood;
	boolean ifGetCode;
	boolean ifGetFood;

}
class FoodPlan extends ReserveSys{
	String planCmd="ads";	
	Food foods = new Food();
	ArrayList<Food> foodAlist=new ArrayList<Food>();
	public void run(Init v){
		v.date=v.strArray[2];
		addFoodPlan(v);
	}
	void addFoodPlan(Init v){
		Scanner datain=new Scanner(System.in);
      while(!planCmd.equals("end")){
      	planCmd = datain.nextLine();
			Food foods2 = new Food();
        	this.commandAnalisys(v,planCmd,foods2);
			foodAlist.add(foods2);
		}
		System.out.println("adding foodPlan successfully Done");
	}
	private void commandAnalisys(Init v,String cmd, Food f){
		v.strArray=cmd.split(" ");
		f.foodName=v.strArray[0];
		f.price=Integer.parseInt(v.strArray[1]);
		f.cookingDate=v.date;
		dateSplit(v.strArray[2],f.deadlineDate);

	}
}


