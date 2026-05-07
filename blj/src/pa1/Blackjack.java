package pa1;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Blackjack {

	public static void WinOrLose(boolean player_bust, int player_sum, boolean house_bust, int house_sum) {
		if(player_bust) System.out.print("[Lose] ");
		else if(house_bust || player_sum>house_sum) System.out.print("[Win] ");
		else if(player_sum==house_sum) System.out.print("[Draw] ");
		else System.out.print("[Lose] ");
	}
    
	public static void main(String[] args) {
        int seed = Integer.parseInt(args[0]);

        Deck deck = new Deck();       // Create the deck.
        deck.shuffle(seed);            // Shuffle the deck.

        /* Your code */
        int player_num=Integer.parseInt(args[1]);
        House house=new House();
        Player player=new Player("Player1");
        Computer []computer=new Computer[player_num-1];
        for(int i=0; i<player_num-1; i++) computer[i]=new Computer("Player"+(i+2));
        
        //2장씩 주기
        for(int i=0; i<2; i++) {
	    	player.AddCard(deck.dealCard());
	    	for(int j=0; j<player_num-1; j++) {
	    		computer[j].AddCard(deck.dealCard());
	    	}
	    	house.AddCard(deck.dealCard());
        }
        //기본 카드 출력
        house.PrintCard();
        player.PrintCard();
        for(int i=0; i<player_num-1; i++) computer[i].PrintCard();
        
        //겜 시작
        if(!house.isBusted()) {
        	System.out.println("\n--- "+player.name+" turn ---");
        	player.PlayOrNot(deck);
        	for(int i=0; i<player_num-1; i++) {
        		System.out.println("\n--- "+computer[i].name+" turn ---");
        		computer[i].PlayOrNot(deck);
        	}
        	System.out.println("\n\n--- "+house.name+" turn ---");
        	house.PlayOrNot(deck);        	
        }
        
        //겜 끝!
        System.out.println("\n--- Game Results ---");
        house.PrintCard();
        WinOrLose(player.isBusted(), player.sum, house.isBusted(), house.sum);
        player.PrintCard();
        for(int i=0; i<player_num-1; i++) {
        	WinOrLose(computer[i].isBusted(), computer[i].sum, house.isBusted(), house.sum);
        	computer[i].PrintCard();
        }
    }
    
}

class Card {
	int value;
	int suit;
    public Card() {}
    public Card(int theValue, int theSuit) {
    	value=theValue;
    	suit=theSuit;
    }
    public String toString() {
    	String card="";
    	if(value==1) card+='A';
    	else if(value==11) card+='J';
    	else if(value==12) card+='Q';
    	else if(value==13) card+='K';
    	else card+=Integer.toString(value);
    	
    	if(suit==0) card+='c';
    	else if(suit==1) card+='h';
    	else if(suit==2) card+='d';
    	else if(suit==3) card+='s';
    	return card;
    }
}

class Deck {
    private Card[] deck=new Card[52];
    private int cardsUsed;
    
    public Deck() {
    	int index=0;
    	for(int i=1; i<=13; i++) {
    		for(int j=0; j<4; j++) deck[index++]=new Card(i, j);
    	}
    }
    
	// 수정 ㄴㄴ-------------------------------
    public void shuffle(int seed) {
        Random random = new Random(seed);
        for (int i = deck.length - 1; i > 0; i--) {
            int rand = (int)(random.nextInt(i + 1));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }

    public Card dealCard() {
        if (cardsUsed == deck.length)
            throw new IllegalStateException("No cards are left in the deck.");

        cardsUsed++;
        return deck[cardsUsed - 1];
    }//수정 ㄴㄴ---------------------------------------
}

class Hand {// Set of cards in your hand 
	/* Your code */ 
	protected ArrayList<Card> cards=new ArrayList<Card>();
	protected int sum;
	protected String name;
	
	public Hand() {}
	public void AddCard(Card deck) { 
		cards.add(deck); 
		Calculate();
	}
	public void Calculate() {
		sum=0;
		int ace=0;
		for(int i=0; i<cards.size(); i++) {
			if(cards.get(i).value==1) {
				sum+=1;
				ace++;
			}
			else if(cards.get(i).value>10) sum+=10;
			else sum+=cards.get(i).value;
		}
		if(ace!=0) {
			if(sum+10<21) sum+=10;
		}
	}
	public boolean isBusted() { return (sum>21); }
	public void PrintCard() {
		System.out.print(name+": ");
			
		for(int i=0; i<cards.size()-1; i++) {
			System.out.print(cards.get(i).toString()+", ");
		}
		System.out.print(cards.get(cards.size()-1).toString()+" ("+sum+")");

		if(isBusted()) System.out.println("- Bust!");
		else System.out.println();
	}
	
}          
class Computer extends Hand { // Player automatically participates
	/* Your code */ 
	public Computer() {}
	public Computer(String name) {
		this.name=name;
	}
	public void PlayOrNot(Deck deck) {
		while(true) {
			Calculate();
			PrintCard();
			
			if(isBusted()) break;
			else if(sum<14) {
				AddCard(deck.dealCard());
				System.out.println("Hit");
			}
			else if(sum>17) {
				System.out.println("Stand");
				PrintCard();
				break;
			}
			else {
				Random rand=new Random();
				int is_hit=(int)(rand.nextInt(2));
				
				if(is_hit==0) {
					System.out.println("Stand");
					PrintCard();
					break;
				}
				else if(is_hit==1){
					AddCard(deck.dealCard());
					System.out.println("Hit");
				}
			}
		}
	}
	
	
}   
class Player extends Hand { // Player you control
	/* Your code */
	public Player() {}
	public Player(String name) {
		this.name=name;
	}
	public void PlayOrNot(Deck deck) {
		
		while(true) {
			Calculate();
			PrintCard();
			
			if(isBusted()) break;
			
			Scanner scn=new Scanner(System.in);
			String doing=scn.next();

			if(doing.equals("Hit")) AddCard(deck.dealCard());
			else if(doing.equals("Stand")) {
				PrintCard();
				break;
			}
		}
	}
	
	
}     
class House extends Hand { 
	boolean firstgame;
	public House() {
		name="House";
		firstgame=true;
	}
	public void PlayOrNot(Deck deck) {
		while(true) {
			Calculate();
			PrintCard();
			if(isBusted()) break;
			if(sum<=16) {
				AddCard(deck.dealCard());
				System.out.println("Hit");
			}
			else {
				System.out.println("Stand");
				PrintCard();
				break;
			}
		}
	}
	public void PrintCard() {
		if(firstgame) {
			System.out.println(name+": Hidden ,"+cards.get(1).toString());
			firstgame=false;
		}
		else {
			super.PrintCard();
		}
	}
	/* Your code */ 
}