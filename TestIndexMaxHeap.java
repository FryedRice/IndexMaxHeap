/**
* @ Description: Java实现变长数组二叉索引堆优先队列数据结构IndexMaxPQ<Item>
* @ Date: Mar.5nd 2020
* @ Author: Jay Sonic
*/
import java.util.Iterator;
import java.util.Random;

public class TestIndexMaxHeap{
	public static void main(String[] args){
		IndexMaxHeap<Integer> testHP = new IndexMaxHeap<Integer>(12);
		Random rd = new Random(1);

		System.out.printf("********入堆测试开始.********\n");
		for(int i=1; i<=20; ++i){
            int j = rd.nextInt(100);
			testHP.insert(j);
			System.out.printf("%3d已入堆.\t",j);
			System.out.printf("N = %3d\t",testHP.size());
			System.out.printf("volume = %3d\n",testHP.capacity());
		}
		System.out.printf("********入堆测试结束.********\n");
        
        System.out.printf("********修改测试开始.********\n");
		for(int i=1; i<=10; ++i){
            int testSize = testHP.size()-1;
            int k = rd.nextInt(testSize)+1;
            int val = rd.nextInt(1000);
			testHP.modify(k,val);
			System.out.printf("第%3d位改成%3d.\t",k,val);
			System.out.printf("Max = %3d\n",testHP.getMax());
			//System.out.printf("volume = %3d\n",testHP.capacity());
		}
		System.out.printf("********修改测试结束.********\n");
        
        System.out.printf("********出堆测试开始.********\n");
        for(int i=1; i<=18; ++i){
            int j = testHP.delMax();
			System.out.printf("%3d已出堆.\t",j);
			System.out.printf("N = %3d\t",testHP.size());
			System.out.printf("volume = %3d\n",testHP.capacity());
		}
        System.out.printf("********出堆测试结束.********\n");
	}
}

class IndexMaxHeap <Item extends Comparable<Item>> {
	private int[] index;
	private Item[] a;
	private int N; //当前元素个数
	private int volume; //当前总容量

	public IndexMaxHeap(int i){
		if(i<10) i = 10; //最小容量10,数组最短11
		index = new int[i+1];
		a = (Item[]) new Comparable[i+1];
		N = 0;
		volume = i;
	}
	public IndexMaxHeap(){
		this(10);
	}

	private void resize(int i){ //调整数组大小o(t)=2N
		int[] tempIndex = new int[i];
		Item[] tempA = (Item[]) new Comparable[i];
		for(int j=1; j<=N; ++j){
			tempIndex[j] = j;
			tempA[j] = a[index[j]];
		}
		index = tempIndex;
		a = tempA;
		volume = i-1;
	}
	private boolean less(int i, int j){
		return a[index[i]].compareTo(a[index[j]]) < 0;
	}
	private void exch(int i, int j){
		int temp = index[i];
		index[i] = index[j];
		index[j] = temp;
	}
	private void floatUP(int i){
		while(i>1 && !less(i,i/2)){//可以上浮
			exch(i,i/2);
			i /= 2;
		}
	}
	private void sinkDN(int i){
		int j;
		while(2*i < N){//还没有沉到叶子节点
			j = 2*i;
			if(j+1<=N && less(j,j+1)) ++j;
			if(!less(i,j)) break;//下沉结束
			exch(i,j);
			i = j;
		}
	}

	public int size(){
		return N;
	}
	public int capacity(){
		return volume;
	}
	public boolean isEmpty(){
		return N==0;
	}
	public Item getMax(){//请调用者自行判断非空
		return a[index[1]];
	}
	public int getMaxIndex(){//请调用者自行判断非空
		return index[1];
	}
	public void insert(Item val){
		if(N==volume) resize(2*volume+1);//遇满先翻倍扩容
		++N;
		index[N] = N;
		a[N] = val;
		floatUP(N);
	}
	public Item delMax(){
		if(volume>=20 && N<=volume/4) resize(volume/2+1);
		Item outcome = a[index[1]];
		exch(1,N);
		--N;
		sinkDN(1);
        return outcome;
	}
	public void modify(int k, Item val){//请调用者自行检查越界
		a[k] = val;
		int i;
		for(i=1; i<N; ++i){//找出k在index[]中的位置
			if(index[i]==k) break;
		}
		sinkDN(i);
		floatUP(i);
	}
}