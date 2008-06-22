var ffm = ffm||{};

(function(ffm){
  ffm.IRC = ffm.IRC||{};

  ////////////////////////////////////////////////////////////////////////////
  /*
   * IRC client
   */
  ffm.IRC.Client = function(jssocket){
    this.socket = jssocket;
  }

  ////////////////////////////////////////////////////////////////////////////
  /*
   * IRC server
   */
  ffm.IRC.Server = function(jssocket){
    this.socket = jssocket;
  }

})(ffm);
