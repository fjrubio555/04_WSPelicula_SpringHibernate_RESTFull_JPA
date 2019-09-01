package tz.franrubio.pelicula.controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.franrubio.pelicula.entities.Pelicula;

/**
 * Controlador RESTFULL PeliculasController.
 * 
 * @author Francisco J. Rubio
 * @version 1.0
 */

@RestController
public class PeliculasController {

    private static EntityManagerFactory emFactory;
    private static EntityManager entityManager;

    @RequestMapping(value = "/getAllPeliculas", method = RequestMethod.GET)
    public List<Pelicula> getAllPeliculas() {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        List<Pelicula> lista = new ArrayList<Pelicula>();
        try {
            lista = entityManager.createNamedQuery("Pelicula.findAll").getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return lista;
    }
    
    @RequestMapping(value = "/getPeliById", method = RequestMethod.GET)
    public Pelicula getPeliById(@RequestParam(name = "id") int id) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        Pelicula pelicula = new Pelicula();
        try {
            Query consulta = entityManager.createNamedQuery("Pelicula.findById");
            consulta.setParameter("id", id);
            pelicula = (Pelicula)consulta.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return pelicula;
    }

    @RequestMapping(value = "/getPelisTitulo", method = RequestMethod.GET)
    public List<Pelicula> getPelisTitulo(@RequestParam(name = "titulo") String titulo) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        List<Pelicula> lista = new ArrayList<Pelicula>();
        try {
            Query consulta = entityManager.createNamedQuery("Pelicula.findByTitulo");
            consulta.setParameter("titulo", '%' + titulo.trim().toLowerCase() + '%');
            lista = consulta.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return lista;

    }

    @RequestMapping(value = "/getPelisDirector", method = RequestMethod.GET)
    public List<Pelicula> getPelisDirector(@RequestParam(name = "director") String director) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        List<Pelicula> lista = new ArrayList<Pelicula>();
        try {
            Query consulta = entityManager.createNamedQuery("Pelicula.findByDirector");
            consulta.setParameter("director", '%' + director.trim().toLowerCase() + '%');
            lista = consulta.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return lista;

    }

    @RequestMapping(value = "/getPelisEstreno", method = RequestMethod.GET)
    public List<Pelicula> getPelisEstreno(@RequestParam(name = "estreno") int estreno) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        List<Pelicula> lista = new ArrayList<Pelicula>();
        try {
            Query consulta = entityManager.createNamedQuery("Pelicula.findByEstreno");
            consulta.setParameter("estreno", estreno);
            lista = consulta.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return lista;

    }
    
    @RequestMapping(value = "/addPelicula", method = RequestMethod.GET)
    public void addPelicula(@RequestParam(name = "titulo") String titulo,
            @RequestParam(name = "director") String director,
            @RequestParam(name = "estreno") int estreno) {

        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            //Comprobar que la pelicula existe o no usando 
            //una consulta de monenclatura Hibernate.
            Query consulta = entityManager.createQuery("from Pelicula where lower(titulo) = :titulo "
                    + " and lower(director) = :director and estreno = :estreno");
            consulta.setParameter("titulo", titulo.trim().toLowerCase());
            consulta.setParameter("director", director.trim().toLowerCase());
            consulta.setParameter("estreno", estreno);
            if (!(consulta.getResultList().size() > 0)) {

                Pelicula pelicula = new Pelicula(titulo.trim(), director.trim(), estreno);
                entityManager.persist(pelicula);
            }

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        //return pelicula;
    }

    @RequestMapping(value = "/deletePelicula", method = RequestMethod.GET)
    public void deletePelicula(@RequestParam(name = "id") int id) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Pelicula pelicula = entityManager.find(Pelicula.class, id);
            //Si existe la pelicula que la borre.
            if (entityManager.contains(pelicula)) {
                entityManager.remove(pelicula);
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }

    }

    @RequestMapping(value = "/updatePelicula", method = RequestMethod.GET)
    public void updatePelicula(@RequestParam(name = "id") int id,
            @RequestParam(name = "titulo") String titulo,
            @RequestParam(name = "director") String director,
            @RequestParam(name = "estreno") int estreno) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Pelicula pelicula = entityManager.find(Pelicula.class, id);
            if (entityManager.contains(pelicula)) {
                pelicula.setTitulo(titulo.trim());
                pelicula.setDirector(director.trim());
                pelicula.setEstreno(estreno);
                entityManager.persist(pelicula);
            }
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
    }
}
