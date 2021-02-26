package fr.diginamic;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

public class ActeurRepositoryTest {
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("movie_db");
	private EntityManager em = emf.createEntityManager();
	
	/**
	 * Extraire tous les acteurs triés dans l'ordre alphabétique des identités
	 */
	@Test
	public void testExtraireActeursTriesParIdentite() {
		
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a ORDER BY a.identite ASC", Acteur.class);
		List<Acteur> acteurs = query.getResultList();
		
		assertEquals(1137, acteurs.size());
		assertEquals("A.J. Danna", acteurs.get(0).getIdentite());
	}
	
	/**
	 * Extraire l'actrice appelée Marion Cotillard
	 */
	@Test
	public void testExtraireActeursParIdentite() {
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a WHERE a.identite LIKE :identite", Acteur.class);
		query.setParameter("identite", "Marion Cotillard");
		List<Acteur> acteurs = query.getResultList();
		
		assertEquals(1, acteurs.size());
		assertEquals("Marion Cotillard", acteurs.get(0).getIdentite());
	}
	
	/**
	 * Extraire la liste des acteurs nés en 1985
	 */
	@Test
	public void testExtraireActeursParAnneeNaissance() {
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a WHERE YEAR(a.anniversaire) = :annee", Acteur.class);
		query.setParameter("annee", 1985);
		List<Acteur> acteurs = query.getResultList();
		
		assertEquals(10, acteurs.size());
	}
	
	/**
	 * Extraire la liste des actrices ayant joué le rôle d'Harley QUINN
	 */
	@Test
	public void testExtraireActeursParRole() {
		
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a JOIN a.roles r WHERE r.nom = ?1", Acteur.class);
		query.setParameter(1, "Harley Quinn");
		List<Acteur> acteurs = query.getResultList();
		
		assertEquals(2, acteurs.size());
		assertEquals("Margot Robbie", acteurs.get(0).getIdentite());
		assertEquals("Margot Robbie", acteurs.get(1).getIdentite());
	}
	
	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film paru en 2015
	 */
	@Test
	public void testExtraireActeursParFilmParuAnnee() {
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a JOIN a.roles r JOIN r.film f WHERE f.annee = :annee", Acteur.class);
		query.setParameter("annee", 2015);

		List<Acteur> acteurs = query.getResultList();
		assertEquals(140, acteurs.size());
	}
	
	/**
	 * Extraire la liste de tous les acteurs français 
	 */
	@Test
	public void testExtraireActeursParPays() {
		TypedQuery<Acteur> query = em.createQuery("SELECT DISTINCT a FROM Acteur a JOIN a.roles r JOIN r.film f JOIN f.pays p WHERE p.nom = :nom", Acteur.class);
		query.setParameter("nom", "France");

		List<Acteur> acteurs = query.getResultList();
		assertEquals(158, acteurs.size());
	}
	
	/**
	 * Extraire la liste de tous les acteurs français ayant joué dans un film paru en 2017
	 */
	@Test
	public void testExtraireActeursParListePaysEtAnnee() {
		TypedQuery<Acteur> query = em.createQuery("SELECT DISTINCT a FROM Acteur a JOIN a.roles r JOIN r.film f JOIN f.pays p "
				+ "WHERE p.nom = ?1 AND f.annee = ?2", Acteur.class);
		query.setParameter(1, "France");
		query.setParameter(2, 2017);

		List<Acteur> acteurs = query.getResultList();
		assertEquals(24, acteurs.size());
	}
	
	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film réalisé par Ridley Scott 
	 * entre les années 2010 et 2020
	 */
	@Test
	public void testExtraireParRealisateurEntreAnnee() {
		TypedQuery<Acteur> query = em.createQuery("SELECT DISTINCT a FROM Acteur a JOIN a.roles role JOIN role.film f JOIN f.realisateurs real "
				+ "WHERE real.identite LIKE ?1 AND f.annee BETWEEN ?2 AND ?3", Acteur.class);
		query.setParameter(1, "%Ridley Scott%");
		query.setParameter(2, 2010);
		query.setParameter(3, 2020);
		
		List<Acteur> acteurs = query.getResultList();
		assertEquals(27, acteurs.size());
	}
}