# -*- coding: utf-8 -*-

def oauth_facebook
  Koala::Facebook::OAuth.new(
    settings.facebook[:app_id],
    settings.facebook[:app_secret],
    create_url(request.scheme, request.host, '/auth/facebook/callback'))
end
