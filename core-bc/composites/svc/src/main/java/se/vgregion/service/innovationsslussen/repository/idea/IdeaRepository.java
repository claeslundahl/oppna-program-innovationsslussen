package se.vgregion.service.innovationsslussen.repository.idea;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

/**
 * Repository interface for managing {@code Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public interface IdeaRepository extends Repository<Idea, Long> {

    /**
     * Find an {@link Idea} with urlTitle specified
     *
     * @param id the id
     * @return an {@link Idea}
     */
	Idea find(long id);
	
	
    /**
     * Find an {@link Idea} with urlTitle specified
     *
     * @param urlTitle the urlTitle
     * @return an {@link Idea}
     */
	Idea findIdeaByUrlTitle(String urlTitle);

    /**
     * Find the number of {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return an int with the number of Idea
     */
    int findIdeasCountByCompanyId(long companyId);
	
	
    /**
     * Find all {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByCompanyId(long companyId);
    
    /**
     * Find {@link Idea}s for a company.
     *
     * @param companyId the companyid
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByCompanyId(long companyId, int start, int offset);
    

    /**
     * Find the number of {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return an int with the number of Idea
     */
    int findIdeaCountByGroupId(long companyId, long groupId);
    
    /**
     * Find all {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId);
    
    /**
     * Find {@link Idea}s for a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset);
    

    /**
     * Find the number of {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId);
    
    
    /**
     * Find all {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId);
    
    /**
     * Find {@link Idea}s for a user in a group in a company.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId, int start, int offset);
    
    /**
     * Find the number of {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    int findUserFavoritedIdeasCount(long companyId, long groupId, long userId);
    
    
    /**
     * Find all {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId);
    
    /**
     * Find {@link Idea}s which a user has added as a favorite.
     *
     * @param companyId the companyid
     * @param groupId   the groupId
     * @param userId   the userId
     * @return a {@link List} of {@link Idea}s
     */
    List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset);
    
    
    /**
     * Remove the {@link Idea} with the id
     *
     * @param ideaId the id of the idea to remove
     * @return void
     */
    void remove(long ideaId);
    
    
}