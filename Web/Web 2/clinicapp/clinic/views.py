from django.shortcuts import get_object_or_404, render, redirect
from django.contrib.auth import authenticate, login, logout
from django.views.generic import ListView, CreateView
from .models import Doctor, Perfil, Pregunta, Cita
from django.views import View
from django.shortcuts import render

class LoginView(View):
    def get(self, request):
        return render(request, 'login.html')

    def post(self, request):
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            return redirect('clinic:citas')
        else:
            # Aquí puedes manejar el caso de que la autenticación falle, 
            # por ejemplo, volviendo a renderizar la página de login con un mensaje de error.
            return render(request, 'login.html', {'error': 'Usuario o contraseña incorrectos'})

class LogoutView(View):
    def get(self, request):
        logout(request)
        return redirect('login')


class DoctorVirtualView(ListView):
    model = Pregunta
    template_name = 'doctor_virtual.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            return Pregunta.objects.filter(medico=None) # solo preguntas sin responder
        else:
            return Pregunta.objects.filter(paciente=self.request.user) # solo preguntas del paciente

class CitasView(ListView):
    model = Cita
    template_name = 'citas.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            doctor = Doctor.objects.get(user=self.request.user)  # Obtén el perfil de Doctor
            return Cita.objects.filter(medico=doctor)  # Filtro por el perfil de Doctor
        else:
            return Cita.objects.filter(paciente=self.request.user)

        

class ResponderPreguntaView(CreateView):
    model = Pregunta
    fields = ['respuesta']
    template_name = 'responder_pregunta.html'
    
    def form_valid(self, form):
        form.instance.medico = self.request.user
        return super().form_valid(form)

class NuevaPreguntaView(CreateView):
    model = Pregunta
    fields = ['titulo', 'descripcion']
    template_name = 'nueva_pregunta.html'
    
    def form_valid(self, form):
        form.instance.paciente = self.request.user
        return super().form_valid(form)

class NuevaCitaView(CreateView):
    model = Cita
    fields = ['fecha', 'hora', 'especialidad', 'medico']
    template_name = 'nueva_cita.html'
    
    def form_valid(self, form):
        form.instance.paciente = self.request.user
        return super().form_valid(form)
    

class CancelarCitaView(View):
    def post(self, request, pk):
        # Obtenemos la cita por su clave primaria (pk).
        cita = get_object_or_404(Cita, pk=pk)
        # Verificamos que el usuario actual tenga permisos para cancelar la cita.
        if request.user == cita.paciente:
            # Si el usuario tiene permisos, cancelamos la cita.
            cita.cancelada = True
            cita.save()
            return redirect('citas')  # Suponiendo que 'citas' es el nombre de la vista a la que quieres redirigir después de cancelar una cita.
        else:
            # Si el usuario no tiene permisos, devolvemos un mensaje de error.
            return render(request, 'error.html', {'message': 'No tienes permiso para cancelar esta cita.'})


