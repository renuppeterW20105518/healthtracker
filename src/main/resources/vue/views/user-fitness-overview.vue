<template id="user-fitness-overview">
  <app-layout>
    <div>
      <h3>Fitness list </h3>
      <ul>
        <li v-for="fitnessgoals in FitnessGoals">
          {{fitnessgoals.id}}: {{fitnessgoals.goal}} for {{fitnessgoals.duration}} minutes
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>

app.component("user-fitness-overview",{
  template: "#user-fitness-overview",
  data: () => ({
    fitnessgoals: [],
  }),
  created(){
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/fitness`)
        .then(res => this.fitnessgoals = res.data)
        .catch(() => alert("Error while fetching fitness goals"));
  }
});

</script>
