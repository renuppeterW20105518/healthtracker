<template id="image-profile">
  <app-layout>
    <div v-if="noImageFound">
      <p> We're sorry, we were not able to retrieve this image.</p>
      <p> View <a :href="'/images'">all images</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noImageFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6">Image Profile</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateImage()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteImageByImageId()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-id">Image ID</span>
            </div>
            <input class="form-control" v-model="image.id" name="id" type="number" readonly placeholder="Image ID"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-title">Title</span>
            </div>
            <input class="form-control" v-model="image.title" name="title" type="text" placeholder="Title"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-description">Description</span>
            </div>
            <input class="form-control" v-model="image.description" name="description" type="text" placeholder="description"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-file-path">Image</span>
            </div>
            <img :src="image.image_file_path" style="object-fit: cover; height: 100%; width: 100%;" alt="Image">
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-UserId">User ID</span>
            </div>
            <input class="form-control" v-model="image.userId" name="userId" type="text" readonly placeholder="userId"/>
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("image-profile", {
  template: "#image-profile",
  data: () => ({
    image: null,
    noImageFound: false,
  }),
  created: function () {
    const imageId = this.$javalin.pathParams["image-id"];
    const url = `/api/images/${imageId}`
    axios.get(url)
        .then(res => this.image = res.data)
        .catch(error => {
          console.log("No image found for id passed in the path parameter: " + error)
          this.noImageFound = true
        });
  },
  methods: {
    updateImage: function () {
      const imageId = this.$javalin.pathParams["image-id"]
      const url = `/api/images/${imageId}`
      axios.patch(url,
          {
            title: this.image.title,
            description: this.image.description,
            image_file_path: this.image.image_file_path,
            userId: this.image.userId
          })
          .then(response =>
              this.image.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Image updated!")
      window.location.href = '/';
    },
    deleteImageByImageId: function () {
      if (confirm("Do you really want to delete?")) {
        const imageId = this.$javalin.pathParams["image-id"]
        const url = `/api/images/${imageId}`
        axios.delete(url)
            .then(response => {
              alert("Image deleted")
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