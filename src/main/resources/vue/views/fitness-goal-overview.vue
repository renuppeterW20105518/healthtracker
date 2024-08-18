<template id="fitness-goal-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Fitness Goal
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
        <form id="addFitness">

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-goal">Goal</span>
            </div>
            <input type="text" class="form-control" v-model="formData.goal" name="goal" placeholder="Goal"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-duration">Duration</span>
            </div>
            <input type="text" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-target">Target</span>
            </div>
            <input type="text" class="form-control" v-model="formData.target" name="target" placeholder="Target"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-status">Status</span>
            </div>
            <input type="text" class="form-control" v-model="formData.status" name="status" placeholder="Status"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-started">Started</span>
            </div>
            <input type="date" class="form-control" v-model="formData.started" name="started" placeholder="Started"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-ended">Ended</span>
            </div>
            <input type="date" class="form-control" v-model="formData.ended" name="ended" placeholder="Ended"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId-started">User Id</span>
            </div>
            <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User Id"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addFitness()">Add Fitness</button>
      </div>
    </div>

    <table class="table table-bordered table-striped table-hover">
      <thead>
      <tr>
        <th scope="col" class="text-center">Goal</th>
        <th scope="col" class="text-center">Duration</th>
        <th scope="col" class="text-center">Target</th>
        <th scope="col" class="text-center">Status</th>
        <th scope="col" class="text-center">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(fitness, index) in paginatedFitness" :key="index">
        <td class="text-center">{{ fitness.goal }}</td>
        <td class="text-center">{{ fitness.duration }}</td>
        <td class="text-center">{{ fitness.target }}</td>
        <td class="text-center">{{ fitness.status }}</td>
        <td class="text-center">
          <a :href="`/fitness/${fitness.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteFitness(fitness, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </a>
        </td>
      </tr>
      </tbody>
    </table>

    <nav aria-label="Fitness Pagination">
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

app.component("fitness-goal-overview", {
  template: "#fitness-goal-overview",
  data() {
    return {
      fitness: [],
      formData: { goal: "", duration: "", target: "", status: "", started: "", ended: "", userId: "" },
      currentPage: 1,
      itemsPerPage: 5,
      hideForm: true
    };
  },
  created() {
    this.fetchFitness();
  },
  computed: {
    paginatedFitness() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.fitness.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.fitness.length / this.itemsPerPage);
    },
  },
  methods: {
    async fetchFitness() {
      try {
        const response = await axios.get("api/fitness");
        this.fitness = response.data;
      } catch (error) {
        alert("Error while fetching fitness");
      }
    },
    addFitness: function() {
      const url = `/api/fitness`;
      axios.post(url,
          {
            goal: this.formData.goal,
            duration: this.formData.duration,
            target: this.formData.target,
            status: this.formData.status,
            started: this.formData.started,
            ended: this.formData.ended,
            userId: this.formData.userId
          })
          .then(response => {
            this.fitness.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteFitness: function (fitness, index) {
      if (confirm('Are you sure you want to delete this fitness goal? This action cannot be undone.')) {
        const fitnessId = fitness.id;
        const url = `/api/fitness/${fitnessId}`;
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
    }
  }
});

</script>