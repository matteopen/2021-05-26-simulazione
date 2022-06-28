package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
	private List<Business> migliore;
	
	public Model() {
		
		this.dao = new YelpDao();
		this.idMap = new HashMap<>();
		this.dao.getAllBusiness(idMap);
	}
	
	public void creaGrafo(String city, Integer year) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		List<Business> businesses = this.dao.getBusiness(city, year, idMap);
		
		Graphs.addAllVertices(this.grafo, businesses);
		
		for(Business source : businesses) {
			for(Business target : businesses) {
				if(!source.equals(target)) {
					double peso = source.getMedia()-target.getMedia();
					if(peso>0.0 && !this.grafo.containsEdge(this.grafo.getEdge(target, source))) {
						Graphs.addEdgeWithVertices(this.grafo, target, source, peso);
					}
					else if(peso<0.0 && !this.grafo.containsEdge(this.grafo.getEdge(source, target))) {
						Graphs.addEdgeWithVertices(this.grafo, source, target, -peso);
					}
				}
			}
		}
		
		System.out.println("Grafo creato!"+"\n");
		System.out.println("#Vertici: "+this.grafo.vertexSet().size()+"\n");
		System.out.println("#Archi: "+this.grafo.edgeSet().size()+"\n");
	}
	
	public int nVertices() {
		return this.grafo.vertexSet().size();
	}
	
	public int nEdges() {
		return this.grafo.edgeSet().size();
	}
	
	
	public List<Integer> getYears(){
		return this.dao.getYears();
	}
	
	public List<String> getCities(){
		return this.dao.getCities();
	}
	
	public double rating(Business business){
		List<Business> resultBusinesses = new ArrayList<>();
		double rating= 0.0;
		double in = 0.0;
		double out = 0.0;
		for(DefaultWeightedEdge d : this.grafo.incomingEdgesOf(business)) {
				in+=this.grafo.getEdgeWeight(d);
		}
		for(DefaultWeightedEdge d : this.grafo.outgoingEdgesOf(business)) {
				out+=this.grafo.getEdgeWeight(d);
		}
		rating = in - out;
			
		return rating;
	}
	
	public Business best() {
		Business bestBusiness = null;
		double bestRating = 0.0;
		for(Business b : this.grafo.vertexSet()) {
			if(this.rating(b)>bestRating) {
				bestRating=this.rating(b);
				bestBusiness=b;
			}
		}
		return bestBusiness;
	}
	
	public Set<Business> businesses(){
		return this.grafo.vertexSet();
	}
	
	public List<Business> cerca(Business partenza, double soglia) {
		Business arrivo = this.best();
		this.migliore = new ArrayList<>();
		List<Business> parziale = new ArrayList<>();
		parziale.add(partenza);
		ricorsione(parziale,soglia,arrivo);
		
		return this.migliore;
	}

	private void ricorsione(List<Business> parziale, double soglia, Business arrivo) {
		//CONDIZIONE DI TERMINAZIONE
		
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			if(this.migliore.size()==0) {
				this.migliore = new ArrayList<>(parziale);
				return;
			}
			else {
				if(parziale.size()<this.migliore.size()) {
					this.migliore = new ArrayList<>(parziale);
					return;
				}
			}
		}
		for(Business b : Graphs.successorListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(b)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), b))>soglia) {
				parziale.add(b);
				ricorsione(parziale, soglia, arrivo);
				parziale.remove(parziale.size()-1);
			}
			}
		}
	}

	
	
	
}
