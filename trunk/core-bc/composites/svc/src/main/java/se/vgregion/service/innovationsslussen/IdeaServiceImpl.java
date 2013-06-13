package se.vgregion.service.innovationsslussen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;
import se.vgregion.service.innovationsslussen.repository.ideacontent.IdeaContentRepository;
import se.vgregion.service.innovationsslussen.repository.ideaperson.IdeaPersonRepository;
import se.vgregion.service.innovationsslussen.util.FriendlyURLNormalizer;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.util.comparator.MessageCreateDateComparator;

/**
 * Implementation of {@link IdeaRestrictedService}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class IdeaServiceImpl implements IdeaService {

    private IdeaRepository ideaRepository;
    private IdeaContentRepository ideaContentRepository;
    private IdeaPersonRepository ideaPersonRepository;
    private BariumService bariumService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IdeaServiceImpl.class);
	
	private static final char[] URL_TITLE_REPLACE_CHARS = new char[] {'.', '/'};
	
    /**
     * Constructor.
     *
     * @param ideaRepository the {@link IdeaRepository}
     * @param ideaContentRepository the {@link IdeaContentRepository}
     * @param ideaPersonRepository the {@link ideaPersonRepository}
     * @param bariumService the {@link BariumService}
     */
    @Autowired
    public IdeaServiceImpl(IdeaRepository ideaRepository, IdeaContentRepository ideaContentRepository, IdeaPersonRepository ideaPersonRepository, BariumService bariumService) {
        this.ideaRepository = ideaRepository;
        this.ideaContentRepository = ideaContentRepository;
        this.ideaPersonRepository = ideaPersonRepository;
        this.bariumService = bariumService;
    }
    
    @Override
    @Transactional(rollbackFor = CreateIdeaException.class)
    public Idea addIdea(Idea idea) {
    	
    	try {
    		
    		idea.setUrlTitle(generateUrlTitle(idea.getTitle()));
    		
    		// Persist idea
    		idea = ideaRepository.merge(idea);
    		
    		// Get references to persisted IdeaContent (public and private)
    		IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
    		IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();
    		
			// Add public discussion
			MBMessageLocalServiceUtil.addDiscussionMessage(
				idea.getUserId(), String.valueOf(ideaContentPublic.getUserId()), ideaContentPublic.getGroupId(), IdeaContent.class.getName(),
				ideaContentPublic.getId(), WorkflowConstants.ACTION_PUBLISH);           
			
			// Add private discussion
			MBMessageLocalServiceUtil.addDiscussionMessage(
				idea.getUserId(), String.valueOf(ideaContentPrivate.getUserId()), ideaContentPrivate.getGroupId(), IdeaContent.class.getName(),
				ideaContentPrivate.getId(), WorkflowConstants.ACTION_PUBLISH);
  		
    		
    	} catch (SystemException e) {
    		LOGGER.error(e.getMessage(), e);
    	} catch (PortalException e) {
    		LOGGER.error(e.getMessage(), e);
    	}

        return idea;
    }    
    
    @Override
    public Idea find(long ideaId) {
        return ideaRepository.find(ideaId);
    }
    
    
    @Override
    public Collection<Idea> findAll() {
    	
        return ideaRepository.findAll();
    }

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId) {
        return ideaRepository.findIdeasByCompanyId(companyId);
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {
    	
        return ideaRepository.findIdeasByGroupId(companyId, groupId);
    }
    
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {
    	
    	Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);
    	
    	if(idea != null) {
        	List<CommentItemVO> commentsListPublic = getPublicComments(idea);
        	List<CommentItemVO> commentsListPrivate = getPrivateComments(idea);
        	
        	idea.getIdeaContentPublic().setComments(commentsListPublic);
        	idea.getIdeaContentPrivate().setComments(commentsListPrivate);
    	}
    	
        return idea;
    }
    
    @Override
    @Transactional
    public void remove(long ideaId) {
    	Idea idea = ideaRepository.find(ideaId);
        ideaRepository.remove(idea);
    }

    @Override
    @Transactional
    public void remove(Idea idea) {
    	
    	System.out.println("IdeaServiceImpl - remove");
    	
    	try {
    		
	    	// Delete message board entries
	    	MBMessageLocalServiceUtil.deleteDiscussionMessages(IdeaContent.class.getName(), idea.getIdeaContentPublic().getId());
	    	MBMessageLocalServiceUtil.deleteDiscussionMessages(IdeaContent.class.getName(), idea.getIdeaContentPrivate().getId());
	    	
	    	// Delete idea
	        ideaRepository.remove(idea);
			
		} catch (PortalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(), e);
		}
    	
    }
    
    @Override
    @Transactional
    public void removeAll() {
//        Collection<Idea> all = ideaRepository.findAll();
//        for (Idea idea : all) {
//            ideaRepository.remove(idea);
//        }
    }
    
    @Override
    public Idea updateIdea(Idea idea) {
    	
//        idea = ideaRepository.merge(idea);

        return idea;
    }    
	
	protected String generateUrlTitle(String title) throws PortalException, SystemException {
		title = title.trim().toLowerCase();

		if (Validator.isNull(title) || Validator.isNumber(title) || title.equals("")) {
			long generatedId = CounterLocalServiceUtil.increment();
			return String.valueOf(generatedId);
		}
		else {
			String urlTitle = FriendlyURLNormalizer.normalize(
				title, URL_TITLE_REPLACE_CHARS);

			boolean isUnique = isUniqueUrlTitle(urlTitle);
			
			if(!isUnique) {
				String newUrlTitle = "";
				
				int i = 2;
				while(!isUnique) {
					newUrlTitle = urlTitle + "-" + i;
					isUnique = isUniqueUrlTitle(newUrlTitle);
					i++;
				}
				urlTitle = newUrlTitle;
			}
			
			return urlTitle;
		}
	}
	
	protected List<CommentItemVO> getComments(IdeaContent ideaContent) {
		
		ArrayList<CommentItemVO> commentsList = new ArrayList<CommentItemVO>();
		
		try {
			
			MBMessageDisplay messageDisplay = null;
			
			try {
				messageDisplay = MBMessageLocalServiceUtil.getDiscussionMessageDisplay(
						ideaContent.getUserId(), ideaContent.getGroupId(), IdeaContent.class.getName(),
						ideaContent.getId(), WorkflowConstants.STATUS_ANY);
			} catch(NullPointerException e) {
				return commentsList;
			}
			
			MBThread thread = messageDisplay.getThread();
			
			long threadId = thread.getThreadId();
			long rootMessageId = thread.getRootMessageId();
			
			MessageCreateDateComparator messageComparator = new MessageCreateDateComparator(false);
			
			List<MBMessage> mbMessages = MBMessageLocalServiceUtil.getThreadMessages(
					threadId, WorkflowConstants.STATUS_ANY, messageComparator);
			
			for(MBMessage mbMessage : mbMessages) {
				 
				String curCommentText = mbMessage.getBody();
				Date createDate = mbMessage.getCreateDate();
				long commentId = mbMessage.getMessageId();
				
				if(commentId != rootMessageId) {
					long curCommentUserId = mbMessage.getUserId();
					User curCommentUser = UserLocalServiceUtil.getUser(curCommentUserId);
					String curCommentUserFullName = curCommentUser.getFullName();
					
					CommentItemVO commentItem = new CommentItemVO();
					commentItem.setCommentText(curCommentText);
					commentItem.setCreateDate(createDate);
					commentItem.setId(commentId);
					commentItem.setName(curCommentUserFullName);

					commentsList.add(commentItem);
				}
			}			
			
		} catch (PortalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return commentsList;
	}
	
	protected List<CommentItemVO> getPublicComments(Idea idea) {
		return getComments(idea.getIdeaContentPublic());
	}
	
	protected List<CommentItemVO> getPrivateComments(Idea idea) {
		return getComments(idea.getIdeaContentPrivate());
	}
    
	protected boolean isUniqueUrlTitle(String urlTitle) {
		boolean isUnique = false;
		
		Idea idea = findIdeaByUrlTitle(urlTitle);
		
		if(idea == null) {
			isUnique = true;
		}
		
		return isUnique;
	}
	

}
