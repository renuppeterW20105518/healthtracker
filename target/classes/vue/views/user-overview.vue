<template id="user-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Users
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addUser">
          <input type="file" @change="onFileChange" />
          <button @click="uploadImage()" v-model="formData.image">Upload Image</button>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="formData.email" name="email" placeholder="Email"/>
          </div>
        </form>
        <button rel="tooltip" title="AddUser" class="btn btn-info btn-simple btn-link" @click="addUser()">Add User</button>
      </div>
    </div>

    <table class="table table-bordered table-striped table-hover">
      <thead>
      <tr>
        <th scope="col" class="text-center">User Name</th>
        <th scope="col" class="text-center">User Email</th>
        <th scope="col" class="text-center">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(user, index) in paginatedUsers" :key="index">
        <td class="text-center">{{ user.name }}</td>
        <td class="text-center">{{ user.email }}</td>
        <td class="text-center">
          <a :href="`/users/${user.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteUser(user, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </a>
        </td>
      </tr>
      </tbody>
    </table>

    <nav aria-label="User Pagination">
      <ul class="pagination justify-content-center mt-3">
        <li class="page-item" :class="{ 'disabled': currentPage === 1 }">
          <a class="page-link" href="#" aria-label="Previous" @click="prevPage">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li class="page-item" v-for="page in totalPages" :key="page" :class="{ 'active': currentPage === page }">
          <a class="page-link" href="#" @click="setPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ 'disabled': currentPage === totalPages }">
          <a class="page-link" href="#" aria-label="Next" @click="nextPage">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </app-layout>
</template>


<script>
app.component("user-overview", {
  template: "#user-overview",
  data() {
    return {
      users: [],
      formData: { name: "", email: ""},
      currentPage: 1,
      itemsPerPage: 5,
      hideForm: true
    };
  },
  created() {
    this.fetchUsers();
  },
  computed: {
    paginatedUsers() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.users.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.users.length / this.itemsPerPage);
    },
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get("api/users");
        this.users = response.data;
      } catch (error) {
        alert("Error while fetching activities");
      }
    }, onFileChange(event) {
      this.image = event.target.files[0];
    },
    uploadImage() {
      const formData = new FormData();
      formData.append("image", this.image);

      axios
          .post("/api/upload-image", formData)
          .then((response) => {
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error);
          });
    },
    addUser: function() {
      const url = `/api/users`;
      axios.post(url,
          {
            name: this.formData.name,
            email: this.formData.email,
          })
          .then(response => {
            this.users.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteUser: function (user, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
        const userId = user.id;
        const url = `/api/users/${userId}`;
        axios.delete(url)
            .then(response =>
                this.users.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    setPage(page) {
      this.currentPage = page;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
  },
});
</script>