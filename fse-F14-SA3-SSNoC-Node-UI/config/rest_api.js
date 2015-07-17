var host_url = "http://localhost:1234/ssnoc";

module.exports = {
  'get_all_users' : host_url + '/users',
  'is_password_valid' : host_url + '/user/',
  'get_user' : host_url + '/user/',
  'Update_a_user_record' : host_url + '/user/',
  'post_new_user' : host_url + '/user/signup',
  'update_user_status' : host_url + '/status/',

  'memory_monitor' : host_url + '/memory',

  'performance_monitor' : host_url + '/performance',

  'SSN_analysis' : host_url + '/usergroups/unconnected/',

  'message' : host_url + '/message/',
  'messages' : host_url + '/messages',

   'upload' : host_url + '/image/',

  'search' : host_url + '/search/'
};
