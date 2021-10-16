package f21as.coursework.coffeshop.core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import f21as.coursework.coffeshop.exceptions.FrequencyException;
import gnu.prolog.database.PrologTextLoaderError;
import gnu.prolog.io.OperatorSet;
import gnu.prolog.io.ParseException;
import gnu.prolog.io.ReadOptions;
import gnu.prolog.io.TermReader;
import gnu.prolog.io.TermWriter;
import gnu.prolog.io.WriteOptions;
import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.Term;
import gnu.prolog.vm.Environment;
import gnu.prolog.vm.Interpreter;
import gnu.prolog.vm.PrologCode;
import gnu.prolog.vm.PrologException;

public class DiscountCalculator {
	
	//contains the output from the Prolog engine as a matrix.
	//per each matching rule, the orders matching such rule..
	private HashMap<String, ArrayList<ArrayList<String>>> orders;
	
	private DiscountCalculator(HashMap<String,ArrayList<ArrayList<String>>> results)
	{
		this.orders = results;
	}
	
	
	public HashMap<String, ArrayList<ArrayList<String>>> getMatchingMatrix()
	{
		return this.orders;
	}
	
	
	//instanciate an object DiscountCalculator with the Prolog code (content) implementing the search of orders from a customer matching some of the discount rules (goals)
	public static DiscountCalculator instanciateDiscountCalculator(String content, ArrayList<String> goals) throws InterruptedException, FrequencyException
	{
		
		//need to store 'content' (Prolog program) into a temp file whose filename is put into textToLoad var
		
		
		
		//the content is saved into a temporary file to be process by the Prolog engine
		String fileName=Utils.randomCodeGenerator(8)+".pl";
		FileManager.writeFile(fileName, content);
		
		
		HashMap orders = new HashMap<String, ArrayList<ArrayList<String>>>(); 
		//for each 
		Iterator<String> iter = goals.iterator();
		int count = 0;
		//for each goal (discount rule) query the Prolog engine to find possible solutions (orders)
		while(iter.hasNext())
		{
			
//			String textToLoad = "prolog_example4.pl";
			String textToLoad = fileName;
			String goalToRun = iter.next();
			
			Environment env = new Environment();
			env.ensureLoaded(AtomTerm.get(textToLoad));
			
			//create a thread for each goal and retrieve all the results from it (runner.orders)
			Runner runner = new Runner("goal"+count, env, false, goalToRun);
			
			runner.start();
			runner.join();
			System.out.println("Results:\n"+runner.orders);
			count++;
			orders.put(goalToRun, runner.orders);
		}
		
		
		//the temporary file is removed at the end of the execution
		FileManager.removeFile(fileName);
		return new DiscountCalculator(orders);
		
	}
	
	
	//the code of the thread Runner has been obtained by modifying the class GoalRunner from the gnuprolog.jar linked to the project.
	//
	private static class Runner extends Thread
	{
		private Environment env;
		private boolean once;
		private String goalToRun;
		private ArrayList<ArrayList<String>> orders;

		public Runner(String name, Environment environment, boolean once, String goalToRun)
		{
			super(name);
			env = environment;
			this.once = once;
			this.goalToRun = goalToRun;
			this.orders = new ArrayList<ArrayList<String>>();
		}

		//check if the solution from Prolog has been previously found with a different variables order, in that case return false otherwise the solution is added		
		private boolean addSolution(ArrayList<String> solution)
		{
			boolean exist=false; 
			Iterator<ArrayList<String>> iter = orders.iterator();
			while(iter.hasNext())
			{
				ArrayList<String> existingSol = iter.next();
				if(solution.equals(existingSol))
				{
					exist=true;
					break;
				}
			}
			if(exist) return false;
			else
			{
				this.orders.add(solution);
				return true;
			}
				
			
		}
		
		@Override
		//also the method run is derived from the class gnu.prolog.test.GoalRunner in gnuprolog.jar
		public void run()
		{
			
			Interpreter interpreter = env.createInterpreter();
			env.runInitialization(interpreter);
			for (PrologTextLoaderError element : env.getLoadingErrors())
			{
				PrologTextLoaderError err = element;
				System.err.println(err);
				// err.printStackTrace();
			}
			StringReader rd = new StringReader(goalToRun);
			TermReader trd = new TermReader(rd, env);
			ReadOptions rd_ops = new ReadOptions(env.getOperatorSet());
			try
			{
				Term goalTerm = trd.readTermEof(rd_ops);

				Interpreter.Goal goal = interpreter.prepareGoal(goalTerm);
				String response;
				do
				{
					long startTime = System.currentTimeMillis();
					int rc = interpreter.execute(goal);
					long stopTime = System.currentTimeMillis();
					env.getUserOutput().flushOutput(null);
					System.out.println("time = " + (stopTime - startTime) + "ms");
					response = "n";
					switch (rc)
					{
						case PrologCode.SUCCESS:
						{
							WriteOptions options = new WriteOptions(new OperatorSet());
							ArrayList<String> _vars = new ArrayList<String>();
							Iterator<String> ivars = rd_ops.variableNames.keySet().iterator();
							while (ivars.hasNext())
							{
								String name = ivars.next();
								System.out.println(name + " = "+rd_ops.variableNames.get(name).toString());
								System.out.println("; ");
								_vars.add(rd_ops.variableNames.get(name).toString());
							}
							Collections.sort(_vars);
							this.addSolution(_vars);
//							this.orders.add(_vars);
							if (once)
							{
								System.out.println("SUCCESS");
								return;
							}
							response = "y";

							if ("a".equals(response))
							{
								interpreter.stop(goal);
								goal = interpreter.prepareGoal(goalTerm);
							}

							if ("n".equals(response))
							{
								return;
							}
							break;
						}
						case PrologCode.SUCCESS_LAST:
						{
							WriteOptions options = new WriteOptions(new OperatorSet());
							ArrayList<String> _vars = new ArrayList<String>();
							Iterator<String> ivars2 = rd_ops.variableNames.keySet().iterator();
							while (ivars2.hasNext())
							{
								String name = ivars2.next();
								System.out.println(name + " = ");
								System.out.println("; ");
								_vars.add(rd_ops.variableNames.get(name).toString());
							}
							Collections.sort(_vars);
							this.addSolution(_vars);
							System.out.println("SUCCESS LAST");
							return;
						}
						case PrologCode.FAIL:
							System.out.println("FAIL");
							return;
						case PrologCode.HALT:
							env.closeStreams();
							System.out.println("HALT");
							System.exit(interpreter.getExitCode());
							return;
					}
				} while (true);
			}
			catch (ParseException ex)
			{
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			catch (PrologException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}
	
	
	public static void main(String[] arg)
	{
		try {
			DiscountCalculator calculator = DiscountCalculator.instanciateDiscountCalculator("",null);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FrequencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
