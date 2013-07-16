package se.vgregion.portal.innovationsslussen.idea.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.innovationsslussen.domain.IdeaConstants;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;
import se.vgregion.service.innovationsslussen.idea.IdeaService;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionChecker;
import se.vgregion.service.innovationsslussen.idea.permission.IdeaPermissionCheckerService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

/**
 * Controller class for the view mode in idea portlet.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
@Controller
@RequestMapping(value = "VIEW")
public class IdeaViewController {

	IdeaService ideaService;
	IdeaPermissionCheckerService ideaPermissionCheckerService;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaViewController.class.getName());

    /**
     * Constructor.
     *
     */
    @Autowired
    public IdeaViewController(IdeaService ideaService, IdeaPermissionCheckerService ideaPermissionCheckerService) {
        this.ideaService = ideaService;
        this.ideaPermissionCheckerService = ideaPermissionCheckerService;
    }    

    /**
     * The default render method.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    @RenderMapping()
    public String showIdea(RenderRequest request, RenderResponse response, final ModelMap model) {

    	ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        String ideaType = ParamUtil.getString(request, "type", "public");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        String returnView = "view_public";
        
        if(!urlTitle.equals("")) {
            Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
            boolean isIdeaUserLiked = ideaService.getIsIdeaUserLiked(companyId, scopeGroupId, userId, urlTitle);
            boolean isIdeaUserFavorite = ideaService.getIsIdeaUserFavorite(companyId, scopeGroupId, userId, urlTitle);
            
            List<CommentItemVO> commentsList = new ArrayList<CommentItemVO>();
            if(ideaType.equals("private")) {
            	commentsList = ideaService.getPrivateComments(idea);
            } else {
            	commentsList = ideaService.getPublicComments(idea);;
            }

            IdeaPermissionChecker ideaPermissionChecker = ideaPermissionCheckerService.getIdeaPermissionChecker(scopeGroupId, userId, idea.getId());
            
            
            model.addAttribute("idea", idea);
            model.addAttribute("commentsList", commentsList);
            model.addAttribute("isIdeaUserFavorite", isIdeaUserFavorite);
            model.addAttribute("isIdeaUserLiked", isIdeaUserLiked);
            
            model.addAttribute("isSignedIn", isSignedIn);
            model.addAttribute("ideaPermissionChecker", ideaPermissionChecker);
            
            if(ideaType.equals("private") && ideaPermissionChecker.getHasPermissionViewIdeaPrivate()) {
            	returnView = "view_private";
            }
        }
        
        

        return returnView;
    }
    
    /**
     * The default render method.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the view
     */
    /*
    @RenderMapping(params = "type=private")
    public String showIdeaPrivate(RenderRequest request, RenderResponse response, final ModelMap model) {

    	ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long scopeGroupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();
        boolean isSignedIn = themeDisplay.isSignedIn();
        
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        if(!urlTitle.equals("") && isSignedIn) {
            Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
            boolean isIdeaUserFavorite = ideaService.getIsIdeaUserFavorite(companyId, scopeGroupId, userId, urlTitle);
            boolean isIdeaUserLiked = ideaService.getIsIdeaUserLiked(companyId, scopeGroupId, userId, urlTitle);
            
            model.addAttribute("idea", idea);
            model.addAttribute("isIdeaUserFavorite", isIdeaUserFavorite);
            model.addAttribute("isIdeaUserLiked", isIdeaUserLiked);
        }

        return "view_private";
    }
    */
    
    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping("someAction")
    public final void someAction(ActionRequest request, ActionResponse response, final ModelMap model) {
    	System.out.println("someAction");
        LOGGER.info("someAction");
        response.setRenderParameter("view", "view");
    }    
    
    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=addComment")
    public final void addComment(ActionRequest request, ActionResponse response, final ModelMap model) {
    	
    	System.out.println("addComment");

        LOGGER.info("addComment");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        int ideaContentType = ParamUtil.getInteger(request, "ideaContentType");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        String comment = ParamUtil.getString(request, "comment", "");
        
        if(!comment.equals("")) {
        	
        	try {
            	Idea idea = ideaService.findIdeaByUrlTitle(urlTitle);
            	
            	long ideaCommentClassPK = -1;
            	
            	if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PUBLIC) {
            		ideaCommentClassPK = idea.getIdeaContentPublic().getId();
            	} else if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE) {
            		ideaCommentClassPK = idea.getIdeaContentPrivate().getId();
            	}
            	
            	ServiceContext serviceContext = ServiceContextFactory.getInstance(Idea.class.getName(), request);
            	
    			User user = UserLocalServiceUtil.getUser(userId);

    			String threadView = PropsKeys.DISCUSSION_THREAD_VIEW;

    			MBMessageDisplay messageDisplay = MBMessageLocalServiceUtil.getDiscussionMessageDisplay(
    				userId, groupId, IdeaContent.class.getName(), ideaCommentClassPK,
    				WorkflowConstants.STATUS_ANY, threadView);

    			MBThread thread = messageDisplay.getThread();

    			long threadId = thread.getThreadId();
    			long rootThreadId = thread.getRootMessageId();
    			
    			String commentContentCleaned = comment;
    			
    			String commentSubject = comment;
    			commentSubject = StringUtil.shorten(commentSubject, 50);
    			commentSubject  += "...";
            	
    			// TODO - validate comment and preserve line breaks
    			MBMessageLocalServiceUtil.addDiscussionMessage(
    					userId, user.getScreenName(), groupId,
    					IdeaContent.class.getName(), ideaCommentClassPK, threadId,
    					rootThreadId, commentSubject, commentContentCleaned,
    					serviceContext);
        		
        	} catch (PortalException e) {
        		LOGGER.error(e.getMessage(), e);
        	} catch (SystemException e) {
        		LOGGER.error(e.getMessage(), e);
        	}
        	
        }

        if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE) {
        	response.setRenderParameter("type", "private");	
        }
        
        response.setRenderParameter("urlTitle", urlTitle);

    }
    
    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=addFavorite")
    public final void addFavorite(ActionRequest request, ActionResponse response, final ModelMap model) {
    	
    	System.out.println("addFavorite");
        LOGGER.info("addFavorite");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        int ideaContentType = ParamUtil.getInteger(request, "ideaContentType");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        if(themeDisplay.isSignedIn()) {
        	ideaService.addFavorite(companyId, groupId, userId, urlTitle);
        }

        if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE) {
        	response.setRenderParameter("type", "private");	
        }
        
        response.setRenderParameter("urlTitle", urlTitle);
    }    
    
    
    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=addLike")
    public final void addLike(ActionRequest request, ActionResponse response, final ModelMap model) {
    	
    	System.out.println("addLike");
        LOGGER.info("addLike");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        int ideaContentType = ParamUtil.getInteger(request, "ideaContentType");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        if(themeDisplay.isSignedIn()) {
        	ideaService.addLike(companyId, groupId, userId, urlTitle);
        }

        if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE) {
        	response.setRenderParameter("type", "private");	
        }
        
        response.setRenderParameter("urlTitle", urlTitle);
    }
    
    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=removeFavorite")
    public final void removeFavorite(ActionRequest request, ActionResponse response, final ModelMap model) {
    	
    	System.out.println("removeFavorite");
        LOGGER.info("removeFavorite");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        int ideaContentType = ParamUtil.getInteger(request, "ideaContentType");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        if(themeDisplay.isSignedIn()) {
        	ideaService.removeFavorite(companyId, groupId, userId, urlTitle);
        }

        if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE) {
        	response.setRenderParameter("type", "private");	
        }
        
        response.setRenderParameter("urlTitle", urlTitle);

    }    

    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=removeLike")
    public final void removeLike(ActionRequest request, ActionResponse response, final ModelMap model) {
    	
    	System.out.println("removeLike");
        LOGGER.info("removeLike");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        int ideaContentType = ParamUtil.getInteger(request, "ideaContentType");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        if(themeDisplay.isSignedIn()) {
        	ideaService.removeLike(companyId, groupId, userId, urlTitle);
        }

        if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE) {
        	response.setRenderParameter("type", "private");	
        }
        
        response.setRenderParameter("urlTitle", urlTitle);
    }
    
    /**
     * Method handling Action request.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     */
    @ActionMapping(params = "action=updateFromBarium")
    public final void updateFromBarium(ActionRequest request, ActionResponse response, final ModelMap model) {
    	
    	System.out.println("updateFromBarium");
        LOGGER.info("updateFromBarium");

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        long userId = themeDisplay.getUserId();
        
        int ideaContentType = ParamUtil.getInteger(request, "ideaContentType");
        String urlTitle = ParamUtil.getString(request, "urlTitle", "");
        
        if(themeDisplay.isSignedIn()) {
        	System.out.println("Should now do update");
        	
        	try {
				ideaService.updateFromBarium(companyId, groupId, urlTitle);
			} catch (UpdateIdeaException e) {
				LOGGER.error(e.getMessage(), e);
			}
        }

        if(ideaContentType == IdeaConstants.IDEA_CONTENT_TYPE_PRIVATE) {
        	response.setRenderParameter("type", "private");	
        }
        
        response.setRenderParameter("urlTitle", urlTitle);
    }    
    
    

}
