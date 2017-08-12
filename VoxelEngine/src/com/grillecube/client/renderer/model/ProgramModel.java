/**
**	This file is part of the project https://github.com/toss-dev/VoxelEngine
**
**	License is available here: https://raw.githubusercontent.com/toss-dev/VoxelEngine/master/LICENSE.md
**
**	PEREIRA Romain
**                                       4-----7          
**                                      /|    /|
**                                     0-----3 |
**                                     | 5___|_6
**                                     |/    | /
**                                     1-----2
*/

package com.grillecube.client.renderer.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.grillecube.client.opengl.GLH;
import com.grillecube.client.opengl.object.GLProgram;
import com.grillecube.client.renderer.camera.CameraProjective;
import com.grillecube.client.renderer.model.instance.ModelInstance;
import com.grillecube.common.Logger;
import com.grillecube.common.maths.Matrix4f;
import com.grillecube.common.resources.R;
import com.grillecube.common.world.entity.Entity;

public class ProgramModel extends GLProgram {

	public static final int MAX_JOINTS = 32;

	private int mvpMatrix;
	private int transfMatrix;
	private int[] jointTransforms;

	private int diffuseMap;

	public ProgramModel() {
		super();
		this.addShader(GLH.glhLoadShader(R.getResPath("shaders/model.fs"), GL20.GL_FRAGMENT_SHADER));
		this.addShader(GLH.glhLoadShader(R.getResPath("shaders/model.vs"), GL20.GL_VERTEX_SHADER));
		this.link();
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "uv");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "jointIDS");
		super.bindAttribute(4, "jointWeights");
	}

	@Override
	public void linkUniforms() {
		this.mvpMatrix = super.getUniform("mvp_matrix");
		this.transfMatrix = super.getUniform("transf_matrix");

		this.jointTransforms = new int[MAX_JOINTS];
		for (int i = 0; i < MAX_JOINTS; i++) {
			this.jointTransforms[i] = super.getUniform("jointTransforms[" + i + "]");
		}

		this.diffuseMap = super.getUniform("diffuseMap");
	}

	public void loadCamera(CameraProjective camera) {
		this.loadUniformMatrix(this.mvpMatrix, camera.getMVPMatrix());
	}

	public void loadModel(Model model) {

	}

	public void loadModelInstance(ModelInstance modelInstance) {
		// joint matrices
		Matrix4f[] jointTransformMatrices = modelInstance.getSkeleton().getBoneTransforms();
		for (int i = 0; i < jointTransformMatrices.length; i++) {

//			Logger.get().log(Logger.Level.ERROR, jointTransformMatrices[i]);
			super.loadUniformMatrix(this.jointTransforms[i], jointTransformMatrices[i]);
		}

		// transformation matrix
		Matrix4f transf = new Matrix4f();
		Entity entity = modelInstance.getEntity();
		transf.translate(entity.getPosition());
		transf.rotateXYZ(entity.getRotation());
		// transf.scale(64.0f);
		this.loadUniformMatrix(this.transfMatrix, transf);
		// System.out.println(transf);

		// the skin
		ModelSkin skin = modelInstance.getModel().getSkins().get(modelInstance.getSkinID());
		skin.bind(GL13.GL_TEXTURE1, GL11.GL_TEXTURE_2D);

		this.loadUniformInteger(this.diffuseMap, 1);

	}
}