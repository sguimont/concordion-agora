class ContentFilters {
  static final String HEADER_PRAGMA = "Pragma";
  static final String HEADER_EXPIRES = "Expires";
  static final String HEADER_CACHE_CONTROL = "Cache-Control";

  def filters = {
    debug(controller: '*', action: '*') {
      before = {
        if (params.debug) {
          session.debug = params.debug
        }
      }
    }

    encoding(controller: '*', action: '*') {
      before = {
        response.setCharacterEncoding("UTF-8");

        response.setHeader(HEADER_PRAGMA, "no-cache");
        response.setDateHeader(HEADER_EXPIRES, 1L);
        response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
        response.addHeader(HEADER_CACHE_CONTROL, "no-store");

        request.setCharacterEncoding("UTF-8");
      }
    }
  }
}