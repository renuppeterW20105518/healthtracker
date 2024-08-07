<template id="activity-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Activities
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm = !hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addActivity">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-name">Description</span>
            </div>
            <input type="text" class="form-control" v-model="formData.description" name="description" placeholder="Description"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-duration">Duration</span>
            </div>
            <input type="text" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-calories">Calories</span>
            </div>
            <input type="text" class="form-control" v-model="formData.calories" name="calories" placeholder="Calories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-activity-started">Started</span>
            </div>
            <input type="date" class="form-control" v-model="formData.started" name="started" placeholder="Started"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId-started">User Id</span>
            </div>
            <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User Id"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addActivity()">Add Activity</button>
      </div>
    </div>
    <table class="table table-bordered table-striped table-hover">
      <thead>
      <tr>
        <th scope="col" class="text-center">Description</th>
        <th scope="col" class="text-center">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(activity, index) in paginatedActivities" :key="index">
        <td class="text-center">{{ activity.description }}</td>
        <td class="text-center">
          <a :href="`/activities/${activity.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteActivity(activity, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </a>
        </td>
      </tr>
      </tbody>
    </table>

    <nav aria-label="Activity Pagination">
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
app.component("activity-overview", {
  template: "#activity-overview",
  data() {
    return {
      activities: [],
      formData: { description: "", duration: "", calories: "", started: "", userId: "" },
      currentPage: 1,
      itemsPerPage: 5,
      hideForm: true
    };
  },
  created() {
    this.fetchActivities();
  },
  computed: {
    paginatedActivities() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.activities.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.activities.length / this.itemsPerPage);
    },
  },
  methods: {
    async fetchActivities() {
      try {
        const response = await axios.get("api/activities");
        this.activities = response.data;
      } catch (error) {
        alert("Error while fetching activities");
      }
    },
    addActivity: function() {
      const url = `/api/activities`;
      axios.post(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            calories: this.formData.calories,
            started: this.formData.started,
            userId: this.formData.userId
          })
          .then(response => {
            this.activities.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteActivity: function (activity, index) {
      if (confirm('Are you sure you want to delete this activity? This action cannot be undone.')) {
        const activityId = activity.id;
        const url = `/api/activities/${activityId}`;
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