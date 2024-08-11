<template id="login-gym">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <h2>Welcome to Nathans Fitness Center</h2>
      </div>
      <div class="card-body">
        <form id="loginForm">
          <div class="row">
            <div class="col-12">Login</div>
            <div class="col-12">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-username">Username</span>
                </div>
                <input type="text" class="form-control" v-model="formData.username" name="username"
                       placeholder="Username" required />
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-password">Password</span>
                </div>
                <input type="password" class="form-control" v-model="formData.password" name="password"
                       placeholder="Password" required />
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col">
              <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
              <button type="button" class="btn btn-info" @click="login">Login</button>
              <a href="/trainee-registration" class="ml-3">Registration</a>
            </div>
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("login-gym", {
  template: "#login-gym",
  data() {
    return {
      formData: {
        username: "",
        password: ""
      },
      errorMessage: ''
    }
  },
  methods: {
    login() {
      if (!this.formData.username) {
        this.errorMessage = 'Username is required!';
      } else if (!this.formData.password) {
        this.errorMessage = 'Password is required!';
      } else {
        this.errorMessage = '';
        const url = `/api/login-gym`;

        axios.post(url, this.formData)
            .then(response => {
              console.log('Login successful', response.data);
              // Handle successful login, e.g., redirect or store user data
            })
            .catch(error => {
              console.log(error);
              this.errorMessage = 'Invalid username or password';
            });
      }
    }
  }
});
</script>
