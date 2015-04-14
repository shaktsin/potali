package com.potaliadmin.impl.service.search;

import com.potaliadmin.dto.web.request.search.SearchRequest;
import com.potaliadmin.dto.web.response.search.CircleDto;
import com.potaliadmin.dto.web.response.search.SearchPostDto;
import com.potaliadmin.dto.web.response.search.SearchResponse;
import com.potaliadmin.dto.web.response.search.SearchUserDto;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.pact.service.search.SearchService;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.post.PostVO;
import com.potaliadmin.vo.user.UserVO;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by shaktsin on 4/12/15.
 */
@Service
public class SearchServiceImpl implements SearchService {

  private static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
  private static String POST_TYPE = "post";
  private static String USER_TYPE = "user";
  private static String CIRCLE_TYPE = "circle";

  @Autowired
  BaseESService baseESService;

  @Override
  public SearchResponse search(SearchRequest searchRequest) {

    if (!searchRequest.validate()) {
      throw new PotaliRuntimeException("Please provide at least 3 characters to give meaningful search");
    }

    SearchResponse searchResponse = new SearchResponse();

    MatchQueryBuilder query =
        QueryBuilders.matchQuery("_all", searchRequest.getToken()).operator(MatchQueryBuilder.Operator.AND);

    SearchHits searchHits = getBaseESService().search(query);
    if (searchHits == null) {
      logger.info("No search result found for token "+searchRequest.getToken());
      searchResponse.setException(true);
      searchResponse.addMessage("No search result found for token "+searchRequest.getToken());
      return searchResponse;
    }

    Set<SearchUserDto> users = new HashSet<SearchUserDto>();
    Set<SearchPostDto> posts = new HashSet<SearchPostDto>();
    Set<CircleDto> circles = new HashSet<CircleDto>();

    for (SearchHit searchHit : searchHits) {

      if (searchHit.getType().equals(POST_TYPE)) {

        try {
          PostVO postVO = (PostVO) getBaseESService().parserResponse(searchHit.getSourceAsString(), PostVO.class);
          SearchPostDto searchPostDto = new SearchPostDto();
          searchPostDto.setId(postVO.getId());
          searchPostDto.setSubject(postVO.getSubject());
          searchPostDto.setType(postVO.getPostType());

          posts.add(searchPostDto);
        } catch (Exception e) {
          logger.info("Parsing error occurred");
          continue;
        }


      }

      if (searchHit.getType().equals(USER_TYPE)) {
        try {
          UserVO userVO = (UserVO) getBaseESService().parserResponse(searchHit.getSourceAsString(), UserVO.class);
          SearchUserDto searchUserDto = new SearchUserDto();
          searchUserDto.setId(userVO.getId());
          searchUserDto.setFirstName(userVO.getFirstName());
          searchUserDto.setLastName(userVO.getLastName());
          searchUserDto.setAccountName(userVO.getAccountName());

          users.add(searchUserDto);
        } catch (Exception e) {
          logger.info("Parsing error occurred");
          continue;
        }



      }


      if (searchHit.getType().equals(CIRCLE_TYPE)) {
        try {
          CircleVO circleVO = (CircleVO) getBaseESService().parserResponse(searchHit.getSourceAsString(), CircleVO.class);
          CircleDto circleDto = new CircleDto();
          circleDto.setId(circleVO.getId());
          circleDto.setName(circleVO.getName());

          circles.add(circleDto);


        } catch (Exception e) {
          logger.info("Parsing error occurred");

        }

      }
    }

    searchResponse.setPosts(posts);
    searchResponse.setUsers(users);
    searchResponse.setCircles(circles);



    return searchResponse;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }
}
