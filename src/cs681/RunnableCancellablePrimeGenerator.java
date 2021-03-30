package cs681;
import java.util.concurrent.locks.*;

public class RunnableCancellablePrimeGenerator extends RunnablePrimeGenerator {

	private boolean done = false;
	private ReentrantLock lock = new ReentrantLock();
	
	public RunnableCancellablePrimeGenerator(long from, long to) {
		super(from, to);
	}

	
	public void setDone(){
		System.out.println("Setting lock in setDone()...");
		lock.lock();
		try {
			done = true;
		} finally {
			System.out.println("Releasing lock in setDone()...");
			lock.unlock();
		}	
	}

	public void generatePrimes(){
		for (long n = from; n <= to; n++) {
			System.out.println("Setting lock in generatePrimes()...");
			lock.lock();
			try {
				if(done) break;
				if( isPrime(n) ){ this.primes.add(n); }
			} finally {
				System.out.println("Releasing lock in generatePrimes()...");
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) {
		RunnableCancellablePrimeGenerator gen = new RunnableCancellablePrimeGenerator(1,100);
		Thread thread = new Thread(gen);
		thread.start();
		gen.setDone();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		gen.getPrimes().forEach( (Long prime)-> System.out.print(prime + ", ") );
		System.out.println("\n" + gen.getPrimes().size() + " prime numbers are found.");
	}
}

