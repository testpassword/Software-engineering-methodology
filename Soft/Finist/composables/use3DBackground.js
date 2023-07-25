import * as THREE from 'three'

const isBlurred = ref(false)

watch(isBlurred, nv => localStorage.setItem('isBlurred', nv.toString()))

const init = () => {
  onMounted(() => {
    isBlurred.value = Boolean(localStorage.getItem('isBlurred'))


    const white = "#ffffff"
    const randomNumber = (min, max) => Math.floor(Math.random() * (max - min + 1) + min)

    const createRedMaterial = () =>
      new THREE.MeshBasicMaterial({ color: "#" + Math.floor(randomNumber(128, 192)).toString(16) + "0000" })
    const materials = [...Array(10)].map(() => createRedMaterial())
    const createHeart = (heartShape, extrudeSettings, isLeft, scale) => {
      let geometry = new THREE.ExtrudeGeometry(heartShape, extrudeSettings)
      let mesh = new THREE.Mesh(geometry, new THREE.MeshBasicMaterial({ color: white }))
      if (isLeft) {
        mesh = new THREE.Mesh(geometry, materials[Math.floor(Math.random() * materials.length)])
        mesh.position.set(-1.67, -0.35, 0)
        mesh.rotation.set(0, 0, Math.PI + Math.PI / 7)
      } else {
        mesh.position.set(0, 0, 0)
        mesh.rotation.set(0, 0, Math.PI - Math.PI / 8)
      }
      mesh.scale.set(scale, scale, scale)
      return mesh
    }

    const createMartisor = (heartLeft, heartRight) => {
      const heartGroup = new THREE.Group()
      heartGroup.add(heartLeft)
      heartGroup.add(heartRight)
      heartGroup.position.set(1, -1.65, -0.02)
      const martisorGroup = new THREE.Group()
      martisorGroup.add(heartGroup)
      martisorGroup.position.set(0, 0, 0)
      return martisorGroup
    }

    const spawnMartisoare = (martisor, scene, count) => {
      const martisoare = []
      for (let i = 0; i < count * 3; i++) {
        const martisorClone = martisor.clone()
        let x = (Math.random() - 0.5) * 100
        let y = (Math.random() - 0.5) * 100
        let z = (Math.random() - 0.5) * 50
        martisorClone.position.set(x, y, z)
        martisorClone.rotation.set(0, (Math.random() * Math.PI) / 6, (Math.random() * Math.PI) / 4)
        scene.add(martisorClone)
        martisoare.push({
          shape: martisorClone,
          x: Math.random(),
          y: Math.random(),
          z: Math.random(),
        })
      }
      return martisoare
    }

    const tick = (renderer, scene, camera, martisoare) => {
      const speed = 0.01
      martisoare.forEach((el) => {
        el.shape.rotation.x += el.x * speed
        el.shape.rotation.y += el.y * 1.5 * speed
        el.shape.rotation.z += el.z * 2.5 * speed
      })
      renderer.render(scene, camera)
      window.requestAnimationFrame(() => tick(renderer, scene, camera, martisoare))
    }

    const scene = new THREE.Scene()
    scene.background = new THREE.Color("#f8f2fb")
    const canvas = document.getElementById('background')
    const heartX = -25
    const heartY = -25
    const heartShape = new THREE.Shape()
    heartShape.moveTo(25 + heartX, 25 + heartY)
    heartShape.bezierCurveTo(25 + heartX, 25 + heartY, 20 + heartX, 0 + heartY, 0 + heartX, 0 + heartY)
    heartShape.bezierCurveTo(-30 + heartX, 0 + heartY, -30 + heartX, 35 + heartY, -30 + heartX, 35 + heartY)
    heartShape.bezierCurveTo(-30 + heartX, 55 + heartY, -10 + heartX, 77 + heartY, 25 + heartX, 95 + heartY)
    heartShape.bezierCurveTo(60 + heartX, 77 + heartY, 80 + heartX, 55 + heartY, 80 + heartX, 35 + heartY)
    heartShape.bezierCurveTo(80 + heartX, 35 + heartY, 80 + heartX, 0 + heartY, 50 + heartX, 0 + heartY)
    heartShape.bezierCurveTo(35 + heartX, 0 + heartY, 25 + heartX, 25 + heartY, 25 + heartX, 25 + heartY)
    const extrudeSettings = {
      depth: 8,
      bevelEnabled: true,
      bevelSegments: 2,
      steps: 2,
      bevelSize: 1,
      bevelThickness: 1,
    }
    const heartRight = createHeart(heartShape, extrudeSettings, false, 0.01)
    const heartLeft = createHeart(heartShape, extrudeSettings, true, 0.01)
    const martisor = createMartisor(heartRight, heartLeft)
    scene.add(martisor)
    const martisoare = spawnMartisoare(martisor, scene, 50)
    const sizes = {
      width: window.innerWidth,
      height: window.innerHeight,
    }
    window.addEventListener("resize", () => {
      sizes.width = window.innerWidth
      sizes.height = window.innerHeight
      camera.aspect = sizes.width / sizes.height
      camera.updateProjectionMatrix()
      renderer.setSize(sizes.width, sizes.height)
      renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))
    })
    const camera = new THREE.PerspectiveCamera(75, sizes.width / sizes.height, 0.1, 100)
    camera.position.z = 30
    scene.add(camera)
    const renderer = new THREE.WebGLRenderer({
      canvas: canvas,
    })
    renderer.setSize(sizes.width, sizes.height)
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))
    tick(renderer, scene, camera, martisoare)
  })
}

export const use3DBackground = () => ({
  isBlurred,
  init
})
