/**
 * 
 */
package com.zenika.business;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.zenika.domain.Contact;

/**
 * @author acogoluegnes
 *
 */
@Service
public class InMemoryContactService implements ContactService {
	
	private AtomicLong idCounter = new AtomicLong();
	
	private Map<Long, Contact> contacts = new LinkedHashMap<Long, Contact>();
	
	public InMemoryContactService() {
		Contact contact = new Contact(idCounter.incrementAndGet(),"Erich","Gamma");
		contacts.put(contact.getId(),contact);
		contact = new Contact(idCounter.incrementAndGet(),"Richard","Helm");
		contacts.put(contact.getId(),contact);
		contact = new Contact(idCounter.incrementAndGet(),"Ralph","Johnson");
		contacts.put(contact.getId(),contact);
		contact = new Contact(idCounter.incrementAndGet(),"John","Vlissides");
		contacts.put(contact.getId(),contact);
	}

	/* (non-Javadoc)
	 * @see com.zenika.business.ContactService#select()
	 */
	@Override
	public Collection<Contact> select() {
		return contacts.values();
	}

	/* (non-Javadoc)
	 * @see com.zenika.business.ContactService#get(java.lang.Long)
	 */
	@Override
	public Contact get(Long id) {
		return contacts.get(id);
	}

	/* (non-Javadoc)
	 * @see com.zenika.business.ContactService#add(com.zenika.domain.Contact)
	 */
	@Override
	public Contact add(Contact contact) {
		contact.setId(idCounter.incrementAndGet());
		contacts.put(contact.getId(), contact);
		return contact;
	}

	/* (non-Javadoc)
	 * @see com.zenika.business.ContactService#update(com.zenika.domain.Contact)
	 */
	@Override
	public Contact update(Contact contact) {
		contacts.put(contact.getId(), contact);
		return contact;
	}

}
