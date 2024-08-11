<template id="fitness-goal-profile">
  <app-layout>
    <div v-if="noFitnessFound">
      <p> We're sorry, we were not able to retrieve this fitness.</p>
      <p> View <a :href="'/fitness'">all fitness</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noFitnessFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6">Fitness Profile</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateFitness()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteFitnessByFitnessId()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-id">Fitness ID</span>
            </div>
            <input class="form-control" v-model="fitness.id" name="id" type="number" readonly placeholder="Fitness ID"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-gaol">Goal</span>
            </div>
            <input class="form-control" v-model="fitness.goal" name="goal" type="text" readonly placeholder="Goal"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-duration">Duration</span>
            </div>
            <input class="form-control" v-model="fitness.duration" name="duration" type="text" placeholder="Duration"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-target">Target</span>
            </div>
            <input class="form-control" v-model="fitness.target" name="target" type="text" placeholder="Target"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-status">Status</span>
            </div>
            <input class="form-control" v-model="fitness.status" name="status" type="text" placeholder="Status"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-started">Started</span>
            </div>
            <input class="form-control" v-model="fitness.started" name="started" type="data" readonly placeholder="Started"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-ended">Ended</span>
            </div>
            <input class="form-control" v-model="fitness.ended" name="ended" type="data" readonly placeholder="Ended"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-UserId">User ID</span>
            </div>
            <input class="form-control" v-model="fitness.userId" name="userId" type="text" readonly placeholder="userId"/>
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>

app.component("fitness-goal-profile", {
  template: "#fitness-goal-profile",
  data: () => ({
    fitness: null,
    noFitnessFound: false,
  }),
  created: function () {
    const fitnessId = this.$javalin.pathParams["fitness-id"];
    const url = `/api/fitness/${fitnessId}`
    axios.get(url)
        .then(res => this.fitness = res.data)
        .catch(error => {
          console.log("No fitness found for id passed in the path parameter: " + error)
          this.nofitnessFound = true
        });
  },
  methods: {
    updateFitness: function () {
      const fitnessId = this.$javalin.pathParams["fitness-id"]
      const url = `/api/fitness/${fitnessId}`
      axios.patch(url,
          {
            goal: this.fitness.goal,
            duration: this.fitness.duration,
            target: this.fitness.target,
            status: this.fitness.status,
            started: this.fitness.started,
            ended: this.fitness.ended,
            userId: this.fitness.userId
          })
          .then(response =>
              this.fitness.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Fitness updated!")
      window.location.href = '/';
    },
    deleteFitnessByFitnessId: function () {
      if (confirm("Do you really want to delete?")) {
        const fitnessId = this.$javalin.pathParams["fitness-id"]
        const url = `/api/fitness/${fitnessId}`
        axios.delete(url)
            .then(response => {
              alert("Fitness deleted")
              window.location.href = '/';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});

</script>