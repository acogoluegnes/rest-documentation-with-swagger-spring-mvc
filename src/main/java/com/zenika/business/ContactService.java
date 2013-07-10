/**
 * 
 */
package com.zenika.business;

import java.util.Collection;

import com.zenika.domain.Contact;

/**
 * @author acogoluegnes
 *
 */
public interface ContactService {

	Collection<Contact> select();
	
	Contact get(Long id);
	
	Contact add(Contact contact);
	
	Contact update(Contact contact);
	
}
