package rest.lifestyle.model;

import rest.lifestyle.dao.LifeStyleDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="health_profile_history") 
@XmlRootElement(name="measure")
@NamedQueries({
	@NamedQuery(name="HealthProfileHistory.findByPersonAndType",
			query="SELECT h FROM HealthProfileHistory h "
				+ "WHERE h.person = :person AND h.measureType = :measureType"),
	@NamedQuery(name="HealthProfileHistory.findByPersonTypeAndId",
			query="SELECT h FROM HealthProfileHistory h "
				+ "WHERE h.person = :person AND h.measureType = :measureType AND h.id = :mid")
})
public class HealthProfileHistory implements Serializable {

	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(generator="sqlite_healthProfileHistory")
	@TableGenerator(name="sqlite_healthProfileHistory", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="health_profile_history")
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name="person_id", referencedColumnName="id")
	private Person person;

	@Column(name = "measure_type")
	private String measureType;

	@Column(name="measure_value")
	private String measureValue;

	@Temporal(TemporalType.DATE)
    @Column(name="timestamp")
    private Date timestamp; 

	/**
	 * Get id
	 * @return
	 */
	@XmlElement(name="mid")
	public int getId() {
		return this.id;
	}

	/**
	 * Get person
	 * @return
	 */
	@XmlTransient
	public Person getPerson() {
		return this.person;
	}

	/**
	 * Get measureType
	 * @return
	 */
	@XmlTransient
	public String getMeasureType() {
		return this.measureType;
	}

	/**
	 * Get measureValue
	 * @return
	 */
	@XmlElement(name="value")
	public String getMeasureValue() {
		return this.measureValue;
	}

	/**
	 * Get timestamp
	 * @return
	 */
	@XmlElement(name="created")
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Set id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Set person
	 * @param person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * Set measureType
	 * @param measureType
	 */
	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	/**
	 * Set measureValue
	 * @param measureValue
	 */
	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}

	/**
	 * Set timestamp
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
     * Find history by person id and measure type
     * @return
     */
    public static List<HealthProfileHistory> getHistoryByPersonAndMeasureType(Person p, String measureType) {
        EntityManager em = LifeStyleDao.instance.createEntityManager();

        List<HealthProfileHistory> list = em.createNamedQuery(
        		"HealthProfileHistory.findByPersonAndType",
        		HealthProfileHistory.class)
        	.setParameter("person", p)
        	.setParameter("measureType", measureType)
        	.getResultList();

        LifeStyleDao.instance.closeConnections(em);

        return list;
    }
    
    /**
     * Find history by person id and measure type
     * @return
     */
    public static HealthProfileHistory getMeasureById(Person p, String measureType, int mid) {
        EntityManager em = LifeStyleDao.instance.createEntityManager();

        HealthProfileHistory measure = new HealthProfileHistory();
        try {
	        measure = em.createNamedQuery(
	        		"HealthProfileHistory.findByPersonTypeAndId",
	        		HealthProfileHistory.class)
	        	.setParameter("person", p)
	        	.setParameter("measureType", measureType)
	        	.setParameter("mid", mid)
	        	.getSingleResult();
        } catch (Exception e) {
        	return null;
        }

        if (measure != null)
        	em.refresh(measure);

        LifeStyleDao.instance.closeConnections(em);
        
        return measure;
    }

    /**
     * Copy health profile to history
     * @param healthProfile
     * @return
     */
    public static HealthProfileHistory copyHealthProfileToHistory(HealthProfile healthProfile) {
    	// Create new history health profile
    	HealthProfileHistory h = new HealthProfileHistory();
		h.setMeasureType(healthProfile.getMeasureType());
		h.setMeasureValue(healthProfile.getMeasureValue());
		h.setPerson(healthProfile.getPerson());
		h.setTimestamp(healthProfile.getTimestamp());
		
		// Persist object
		EntityManager em = LifeStyleDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        h = em.merge(h);
        tx.commit();
        LifeStyleDao.instance.closeConnections(em);
        
        return h;
    }
}
