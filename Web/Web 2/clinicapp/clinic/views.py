import datetime
from django.shortcuts import redirect, render
from django.contrib.auth.views import LoginView
from .models import Question, Appointment

def doctor_virtual(request):
    questions = Question.objects.filter(answered=False)
    return render(request, 'clinic/doctor_virtual.html', {'questions': questions})

def citas(request):
    if request.user.is_authenticated:
        if request.user.groups.filter(name='Doctors').exists():
            appointments = Appointment.objects.filter(date=datetime.date.today())
            return render(request, 'clinic/doctor_citas.html', {'appointments': appointments})
        else:
            appointments = Appointment.objects.filter(user=request.user)
            return render(request, 'clinic/patient_citas.html', {'appointments': appointments})
    else:
        return redirect('login')