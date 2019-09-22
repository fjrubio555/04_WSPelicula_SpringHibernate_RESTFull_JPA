package tz.franrubio.pelicula.controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.franrubio.pelicula.entities.Pelicula;

/**
 * Controlador RESTFULL PeliculasController.
 *
 *  Aqui se desarrollan los diferentes servicios RestFul del Servidor Web.
 * 
 * @author Francisco J. Rubio
 * @version 1.0
 */

@RestController
// Esta etiqueta nos permite dar acceso a todos a los servicios. Evitando los 
// problemas de autentificacion CORS.
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class PeliculasController {

    private static EntityManagerFactory emFactory;
    private static EntityManager entityManager;
    
    /**
     * El servicio getAllPeliculas, nos permite sacar un listado de todos
     * los registros de la tabla Película de la base de datos.
     * 
     * @return lista
     */
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

    /**
     * El servicio getPelisById nos permite obtener los datos de un registro
     * concreto gracias al identificador que le hemos pasado por parámetro.
     * 
     * @param id
     * 
     * @return pelicula
     */
    
    @RequestMapping(value = "/getPeliById", method = RequestMethod.GET)
    public Pelicula getPeliById(@RequestParam(name = "id") int id) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        Pelicula pelicula = new Pelicula();
        try {
            Query consulta = entityManager.createNamedQuery("Pelicula.findById");
            consulta.setParameter("id", id);
            pelicula = (Pelicula) consulta.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return pelicula;
    }

    /**
     * El servicio getPelisTitulo no devuelve las peliculas cuyo título
     * contengan el texto que le hemos enviado por el parámetro titulo.
     * 
     * @param titulo
     * 
     * @return lista
     */
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

    /**
     * El servicio getPelisDirector no devuelve las peliculas cuyo director
     * contengan el texto que le hemos enviado por el parametro director.
     * 
     * @param director
     * 
     * @return lista
     */
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

    /**
     * El servicio getPelisEstreno no devuelve las peliculas cuyo año de estreno
     * sea el mismo que le hemos enviado por el parametro estreno.
     * 
     * @param estreno
     * 
     * @return lista
     */
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
    
    /**
     * El servicio addPelicula añade una nueva pelicula a la tabla de la base
     * de datos pelicula, tras pasarle los parárametros titulo, director y 
     * estreno. Tambíen comprueba que la película introducida no existe. Y nos 
     * devuelve la respuesta de la operación. Tanto si ha sido correcta la 
     * inserción como si no.
     * 
     * @param titulo
     * @param director
     * @param estreno
     * 
     * @return respuesta
     */
    @RequestMapping(value = "/addPelicula", method = RequestMethod.GET)
    public ResponseEntity<Pelicula> addPelicula(@RequestParam(name = "titulo") String titulo,
                                                @RequestParam(name = "director") String director,
                                                @RequestParam(name = "estreno") int estreno) {

        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        Pelicula pelicula =null;
        ResponseEntity<Pelicula> respuesta = null;
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

                pelicula = new Pelicula(titulo.trim(), director.trim(), estreno);
                entityManager.persist(pelicula);
                respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.CREATED); //201 Añadida
            } else if (consulta.getResultList().size() > 0)  {
                respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.FOUND); //302 Pelicula ya existe
            }

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.INTERNAL_SERVER_ERROR); //500 Error de Comunicación Server
            ex.printStackTrace();
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return respuesta;
    }

    /**
     * El servicio borra una película de la base de datos siempre y cuando el 
     * parámetro de identificación que se le ha suministrado existe. Y nos 
     * devuelve si la operación ha sido correcta o no.
     * @param id
     * 
     * @return respuesta
     */
    @RequestMapping(value = "/deletePelicula", method = RequestMethod.GET)
    public ResponseEntity<Pelicula> deletePelicula(@RequestParam(name = "id") int id) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        Pelicula pelicula =null;
        ResponseEntity<Pelicula> respuesta = null;
        try {
            entityManager.getTransaction().begin();
            pelicula = entityManager.find(Pelicula.class, id);
            //Si existe la pelicula que la borre.
            if (entityManager.contains(pelicula)) {
                entityManager.remove(pelicula);
                respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.OK); //200 Borrado
            }else {
                respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.NOT_FOUND); //404 Película no encontrada
            }
                
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.INTERNAL_SERVER_ERROR); //500 Error de conexión.
            ex.printStackTrace();
            
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return respuesta;
    }

    /**
     * Este servicio modifica la pelicula tras buscar por su identificador,
     * si existe hace la modificación gracias a los parámetros que hemos 
     * introducido y nos devuelve si la operación se ha realizado bien o no.
     * 
     * @param id
     * @param titulo
     * @param director
     * @param estreno
     * 
     * @return respuesta
     */
    @RequestMapping(value = "/updatePelicula", method = RequestMethod.POST)
    public ResponseEntity<Pelicula> updatePelicula(@RequestParam(name = "id") int id,
            @RequestParam(name = "titulo") String titulo,
            @RequestParam(name = "director") String director,
            @RequestParam(name = "estreno") int estreno) {
        emFactory = Persistence.createEntityManagerFactory("PU_Peliculas");
        entityManager = emFactory.createEntityManager();
        Pelicula pelicula =null;
        ResponseEntity<Pelicula> respuesta = null;
        try {
            entityManager.getTransaction().begin();
            pelicula = entityManager.find(Pelicula.class, id);
            if (entityManager.contains(pelicula)) {
                pelicula.setTitulo(titulo.trim());
                pelicula.setDirector(director.trim());
                pelicula.setEstreno(estreno);
                entityManager.persist(pelicula);
                respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.OK); //200
            } else {
                respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.NOT_FOUND); //404
            }
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            respuesta = new ResponseEntity<Pelicula>(pelicula, HttpStatus.INTERNAL_SERVER_ERROR); //500
        } finally {
            entityManager.close();
            emFactory.close();
        }
        return respuesta;
    }
}
