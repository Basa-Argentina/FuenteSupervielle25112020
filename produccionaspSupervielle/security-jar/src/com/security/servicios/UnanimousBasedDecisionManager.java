package com.security.servicios;

import java.util.List;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.security.vote.UnanimousBased;
/**
 * 
 * @author Federico Muñoz
 *
 */
public class UnanimousBasedDecisionManager extends UnanimousBased{
	@SuppressWarnings("unchecked")
	@Override
	public void  decide(Authentication authentication, Object object,ConfigAttributeDefinition config) throws AccessDeniedException {
        int grant = 0;
        int abstain = 0;
        for(AccessDecisionVoter voter : (List<AccessDecisionVoter>)getDecisionVoters()) {
            int result = voter.vote(authentication, object, config);
            switch (result) {
            case AccessDecisionVoter.ACCESS_GRANTED:
                grant++;
                break;
            case AccessDecisionVoter.ACCESS_DENIED:
                throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied",
                        "Access is denied"));
            default:
                abstain++;
                break;
            }
        }
        // To get this far, there were no deny votes
        if (grant > 0) {
            return;
        }
        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }
}
