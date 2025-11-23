package pl.wsb.fitnesstracker.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> findEventsFromCountry(String country) {
        return entityManager.createQuery("FROM Event e WHERE e.country LIKE :country")
                .setParameter("country", country)
                .getResultList();

    }
}
