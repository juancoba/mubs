package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;

import java.lang.Boolean;
import java.util.*;
import models.*;


public class Application extends Controller {

    public static Form<Post> postForm = Form.form(Post.class);
    public static Form<User> signupForm = Form.form(User.class);
    public static String loggedUser;
    public static Boolean changePassword = false;


    public static Result index() {
        return ok(views.html.index.render(Post.byId().findList(), postForm, loggedUser));

    }

    public static Result toNewPostPage(){
        return ok(views.html.newpost.render(loggedUser));
    }

    public static Result addNewPost(){
        Form<Post> filledForm = postForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(views.html.index.render(Post.byId().findList(), filledForm,loggedUser));
        } else {
            Post.create(filledForm.get());
            return redirect(routes.Application.index());
        }
    }

    public static Result showDeletePostPage(){
        return ok(views.html.deleteposts.render(Post.byId().findList(), postForm, loggedUser));
    }

    public static Result deletePost(Long id) {
        Post.delete(id);
        return redirect(routes.Application.showDeletePostPage());
    }

    public static Result editPost(Long id){
        Form<Post> editForm = postForm.fill(
                Post.find.byId(id)
        );
        return ok(views.html.edit.render(id, editForm, loggedUser));
    }

    public static Result updatePost(Long id){
        Form<Post> updateForm = postForm.bindFromRequest();
        if( updateForm.hasErrors()){
            return badRequest(views.html.edit.render(id, updateForm, loggedUser));
        } else {
            Post.delete(id);
            Post.create(updateForm.get());
            return redirect(routes.Application.index());
        }
    }

    public static Result signup(){
        return ok(views.html.signup.render(signupForm));
    }

    public static Result addUser(){
        Form<User> filledForm = signupForm.bindFromRequest();
        String enteredUsername = filledForm.get().username;
        String enteredPassword = filledForm.get().password;
        String enteredRepeatPassword = filledForm.field("repeatPassword").value();


        if(enteredPassword.contentEquals("") || enteredRepeatPassword.contentEquals("")){
            return badRequest(views.html.signup.render(filledForm));
        }

        if(!(enteredPassword.contentEquals(enteredRepeatPassword))){
            filledForm.reject("repeatPassword","Passwords did not match!");
        }


        boolean uniqueUser = User.uniqueUser(enteredUsername);
        if(!uniqueUser){
            filledForm.reject("username", "Sorry, that username is already taken!");
        }

        if(filledForm.hasErrors()) {
            return badRequest(views.html.signup.render(filledForm));
        }
        User.create(filledForm.get());
        return redirect(routes.Application.login());
    }

    public static Result login(){

            Form<User> filledForm = signupForm.bindFromRequest();
            String formUsername = filledForm.get().username;
            String formPassword = filledForm.get().password;

            if (formUsername.contentEquals("") || formPassword.contentEquals("")) {
                return badRequest(views.html.login.render(filledForm));
            }
            User user = User.authenticate(formUsername, formPassword);
            if (user != null) {
                loggedUser = formUsername;
                return redirect(routes.Application.index());
            } else {
                filledForm.reject("password", "Incorrect password or username");
            }
            return badRequest(views.html.login.render(filledForm));
    }

    public static Result goToLogin(){
        return ok(views.html.login.render(signupForm));
    }

}


