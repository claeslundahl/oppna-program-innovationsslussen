<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<ul class="simple-tabs clearfix">
	<li class="current">
		<span>&Ouml;ppen diskussion</span>
	</li>
	<li>
		<a href="">St&auml;ngd diskussion</a>
	</li>
</ul>

<div class="idea-comments">

	<portlet:actionURL var="addCommentUrl" name="addComment" />

	<div class="add-comment">
		<aui:form action="${addCommentUrl}" cssClass="add-comment-form clearfix">
		
			<div class="field-wrap">
				<label for="<portlet:namespace />comment">
					L&auml;gg till din kommentar
				</label>
				<div class="field-element-wrap">
					<textarea name="<portlet:namespace />comment" id="<portlet:namespace />comment"></textarea>
					<p class="notice">
						Notera att denna kommentar kommer att synas &ouml;ppet f&ouml;r alla bes&ouml;kare p&aring; siten.
					</p>	
				</div>
			</div>
			
			<aui:button-row>
				<aui:button type="submit" value="Posta" cssClass="rp-button" />
			</aui:button-row>
		
		</aui:form>
	</div>
	
	<div class="comment clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Anders Andersson
			</div>
			<div class="comment-author-title">
				Id&eacute;givare
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
	<div class="comment comment-alt clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Bengt Bengtsson
			</div>
			<div class="comment-author-title">
				Medlem
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
	<div class="comment clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Carl Carlsson
			</div>
			<div class="comment-author-title">
				Medlem
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
	<div class="comment comment-alt clearfix">
		<div class="comment-author">
			<div class="comment-author-name">
				Anders Andersson
			</div>
			<div class="comment-author-title">
				Id&eacute;givare
			</div>
		</div>
		<div class="comment-entry">
			<div class="comment-entry-date">
				2013-04-23 kl. 12.15
			</div>
			<div class="comment-entry-text">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas erat ante, mollis at feugiat nec, tempor ac massa. Nam sapien risus, pharetra hendrerit laoreet nec, semper vitae elit Maecenas erat ante, mollis at feugiat nec. 
			</div>
		</div>
	</div>
	
</div>

<div class="rp-paging clearfix">
	<ul>
		<li class="previous">
			<a class="arrowleft" href="" title="F&ouml;reg&aring;ende">F&ouml;reg&aring;ende</a>
		</li>
		<li>
			<span class="current">1</span>
		</li>
		<li>
			<a href="">2</a>
		</li>
		<li>
			<a href="">3</a>
		</li>
		<li>
			<a href="">4</a>
		</li>
		<li>
			<a href="">5</a>
		</li>
		<li class="next">
			<a class="arrowright" href="" title="N&auml;sta">N&auml;sta</a>
		</li>
	</ul>
</div>			