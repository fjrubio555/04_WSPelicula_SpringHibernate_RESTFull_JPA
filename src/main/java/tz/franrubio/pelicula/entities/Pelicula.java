package tz.franrubio.pelicula.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entidad Pelicula.
 * 
 * Se recoge la tabla Película de la base de datos, asi como las posibles
 * consultas que se pueden hacer.
 * 
 * @author Francisco J. Rubio
 * @version 1.0
 */
@Entity
@Table(name = "pelicula")
@NamedQueries({
    @NamedQuery(name = "Pelicula.findAll", query = "SELECT p FROM Pelicula p"),
    @NamedQuery(name = "Pelicula.findById", query = "SELECT p FROM Pelicula p WHERE p.id = :id"),
    @NamedQuery(name = "Pelicula.findByTitulo", query = "SELECT p FROM Pelicula p WHERE LOWER(p.titulo) LIKE :titulo"),
    @NamedQuery(name = "Pelicula.findByDirector", query = "SELECT p FROM Pelicula p WHERE LOWER(p.director) like :director"),
    @NamedQuery(name = "Pelicula.findByEstreno", query = "SELECT p FROM Pelicula p WHERE p.estreno = :estreno")})
public class Pelicula implements Serializable {
    
    // Mapeado de los campos de la Tabla.
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "director")
    private String director;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estreno")
    private int estreno;

    //Constructores.
    public Pelicula() {
    }

    public Pelicula(Integer id) {
        this.id = id;
    }

    public Pelicula(String titulo, String director, int estreno) {
       
        this.titulo = titulo;
        this.director = director;
        this.estreno = estreno;
    }

    public Pelicula(Integer id, String titulo, String director, int estreno) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.estreno = estreno;
    }

    //Getter y los Setter.
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getEstreno() {
        return estreno;
    }

    public void setEstreno(int estreno) {
        this.estreno = estreno;
    }
    
    //Sobreescrituras básicas.
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pelicula)) {
            return false;
        }
        Pelicula other = (Pelicula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tz.franrubio.pelicula.entities.Pelicula[ id=" + id + " ]";
    }

}
